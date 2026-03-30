
# Environment_Guide（AWS + サーバ統合版 / 検証・実証向け最小構成）

## 本ドキュメントについて（重要）

本ガイドは **実証（PoC / 検証）を目的とした最小構成** を前提としています。  
可用性・冗長性・高度なセキュリティ対策を施した **本番運用構成ではありません**。

- 本番利用時は、構成・インスタンスタイプ・ネットワーク設計・認証方式を
  **利用者自身の要件に応じて必ず見直してください**
- 本ドキュメントは **OSS（MIT License）として公開することを前提** にしています
- 本ドキュメントは **「AWS Certified Cloud Practitioner」相当の知識がある方** を対象としています。

---

## 基本方針（検証向け）

- 単一AZ・単一EC2構成
- 冗長化・Auto Scaling は行わない
- 管理アクセスは **SSM Session Manager を利用（SSH不使用）**
- 「危険なデフォルト」は採用しない

---

# 1. AWS環境構成（最小構成）

## 1.1 管理アクセス方式（SSM Session Manager）

### 方針

- EC2への管理アクセスは **AWS Systems Manager Session Manager** を利用
- **SSH（22番ポート）は使用しない**
- 秘密鍵（pemファイル）の管理は不要

### 必須設定

- EC2に以下IAM Roleを付与

```
AmazonSSMManagedInstanceCore
```

- Security Groupで22番ポートは開放しない
- Roleを付与した後に接続を反映させるには、EC2の再起動が必要。

---

## 1.2 WEB + DB サーバ（Amazon EC2）

| 項目 | 設定 |
|---|---|
| AMI | Amazon Linux 2023 |
| Instance Type | t3.small |
| 構成 | Spring Boot + PostgreSQL（同居） |
| 管理アクセス | SSM Session Manager |

※ **検証用途のため同居構成**  
※ 本番では DB分離（RDS）を推奨

---

## 1.3 バッチサーバ（Amazon EC2）

| 項目 | 設定 |
|---|---|
| AMI | Amazon Linux 2023 |
| Instance Type | t3.micro |
| 管理アクセス | SSM Session Manager |

---

## 1.4 ストレージ

### Amazon S3（GTFS Realtime公開用）

| 項目 | 設定 |
|---|---|
| パブリックアクセスブロック | **無効（OFF）** |
| バージョニング | 有効（推奨） |
| 暗号化 | SSE-S3 |

### ポリシー作成例：
```
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "AllowPublicDownload",
      "Effect": "Allow",
      "Principal": "*",
      "Action": "s3:GetObject",
      "Resource": "arn:aws:s3:::example/*"
    },
    {
      "Sid": "AllowPutOnlyFromSpecificVPC",
      "Effect": "Allow",
      "Principal": "*",
      "Action": "s3:PutObject",
      "Resource": "arn:aws:s3:::example/*",
      "Condition": {
        "StringEquals": {
          "aws:sourceVpce": "vpce-xxxxxxx"
        }
      }
    }
  ]
}
```
注意：
- このバケットはパブリックアクセスが有効のため、全てのユーザーがオブジェクトを参照できます。
- 不要なオブジェクトや機密情報を含めないよう注意してください。
- バケット内のデータは誰でもダウンロード可能であることを理解してください。
- 将来的に特定のユーザーやサービスのみアクセスを許可する場合は、バケットポリシーやIAMロールで制御してください。
---

## 1.5 GPSトラッカー受信（Lambda）

- Runtime：Java 17
- 実行方式：Function URL
- 認証方式：リクエストのボディに含まれる accessKey を DB で照合して認証

### Lambda用IAM設定

- Lambdaに以下IAM Roleを付与

```
AWSLambdaVPCAccessExecutionRole
```
- Lambda関数は固定IPアドレスを持たないため、プライベートサブネット内のリソース（DB）にアクセスする場合は、Lambda関数を対象のVPCに接続する必要があります。
- この場合、IAMロールには VPCアクセス用の権限 が必要になります（上記の AWSLambdaVPCAccessExecutionRole がこれを含みます）。

---

## 1.6 EC2用IAM設定（最小権限）

### ポリシー作成例

- S3アップロード用
```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "s3:PutObject",
                "s3:PutObjectAcl"
            ],
            "Resource": "arn:aws:s3:::example-bucket/*"
        }
    ]
}
```
- SESメール送信用
```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "ses:SendEmail",
                "ses:SendRawEmail"
            ],
            "Resource": "*"
        }
    ]
}
```
### IAMユーザ作成（アクセスキー用）

- IAMユーザ作成
- 作成したポリシーをアタッチ
- アクセスキー（Access Key ID / Secret Access Key）を作成

※ サーバ内で認証プロファイル設定用に使用

---

# 2. サーバ環境構築ガイド（共通編）

※ 以下の作業は **SSM Session Manager で接続後** に実施します

## 2.1 タイムゾーン設定

```bash
sudo timedatectl set-timezone Asia/Tokyo
```

---

## 2.2 Java（Amazon Corretto 17）

```bash
sudo dnf install -y java-17-amazon-corretto-headless
```

---

## 2.3 Maven

```bash
cd /usr/local/src
sudo wget https://downloads.apache.org/maven/maven-3/3.x.x/binaries/apache-maven-3.x.x-bin.tar.gz
sudo tar xvf apache-maven-3.x.x-bin.tar.gz
sudo ln -s apache-maven-3.x.x maven
```

必要に応じてPATHに追加します
```bash
vi /etc/profile.d/maven.sh
# ---------- maven.sh の内容 -------------------
export M2_HOME=/opt/maven
export PATH=${M2_HOME}/bin:${PATH}
# ---------------------------------------------

source /etc/profile.d/maven.sh
```
---

## 2.4 Git

```bash
sudo dnf install -y git
```

---

## 2.5 cron

```bash
sudo dnf install -y cronie
sudo systemctl enable --now crond
```

## 2.6 AWS CLI プロファイル設定

各機能に対応する IAM ユーザの Access Key ID と Secret Access Key を使って、プロファイルを作成します。

AWS CLI プロファイル設定（SES 用）：

```bash
sudo aws configure --profile wst-ses-user
```

AWS CLI プロファイル設定（S3 用）：

```bash
sudo aws configure --profile s3user
```

---

# 3. pom.xml 設定（環境依存・重要）

本プロジェクトでは、**環境に応じて pom.xml の一部設定を必ず修正**してください。  
以下は **修正が必要になる代表的な設定項目です。**

---

## 3.1 Maven Profile

```xml
<profiles>
  <profile>
    <id>dev</id>
  </profile>
  <profile>
    <id>prod</id>
  </profile>
</profiles>
```

**用途**
- 環境（検証 / 利用者環境）ごとに設定を切り替える

**補足**
- DB接続先、S3バケット名、メール送信元を切り替えるため

---

## 3.2 S3 バケット名

```xml
<s3.bucket.name>xxxx-oss-bus</s3.bucket.name>
```

**補足**
- S3バケット名は AWS全体で一意
- OSSのサンプル値はそのまま使用できない

---

## 3.3 SES 送信元メールアドレス

```xml
<aws.ses.from.email>noreply@example.com</aws.ses.from.email>
```

**補足**
- SESでは送信元の検証が必須
- 未検証アドレスでは送信不可

---

## 3.4 ビルド成果物名（任意）

```xml
<artifactId>bus-service-management-system</artifactId>
```

---

## 3.5 DB接続先

```xml
<jdbc.url><![CDATA[jdbc:postgresql://x.x.x.x:5432/xxxx]]></jdbc.url>
<jdbc.username>xxxx</jdbc.username>
<jdbc.password>xxxx</jdbc.password>
```
---

## 3.6 Redis接続先

```xml
<redis.host>localhost</redis.host>
```

**補足**
- サーバ内部で使用する場合は、localhostのままで問題ありません。
- 外部サービス（例：ElastiCache）を利用する場合は、ホスト名・パスワード・ポート番号を適宜修正してください。
---

## 3.7 log出力先

```xml
<mvn.log.dir>/var/log/xxxx</mvn.log.dir>
```

**重要**
- 事前にログ出力の為のディレクトリをサーバ内に必ず作成してください。
---

### 記載しない設定について（重要）

以下は **pom.xml に記載しません**。

- AWS Access Key / Secret Key
---

# 4. WEBサーバ構築（検証向け）

## 4.1 Redis

```bash
sudo dnf install -y redis6
sudo systemctl enable --now redis6
```

---

## 4.2 PostgreSQL

```bash
sudo dnf install -y postgresql16-server
```
データベース初期化（日本語ロケール設定）：

```bash
sudo su - postgres

initdb --locale=ja_JP.UTF-8 -E UTF8

exit
```
サービス起動・自動起動設定：

```bash
sudo systemctl start postgresql

sudo systemctl enable postgresql
```
### テーブル・初期データ作成sqlの実行

SQLスクリプトファイル `create_table.sql` と `insert_default_data.sql` は `src/main/resources/backup` フォルダに配置されています。
これらのファイルを対象に実行してください。<br>
※ `insert_default_data.sql` の実行前にファイル内の DEFAULT_USER_ID を適宜修正してください。

※ psql のプロンプト上で実行してください。

DB作成（pom.xml確認）：
```bash
CREATE DATABASE mlit_community_bus_kit;
```

スクリプトファイル実行例：
```bash
\i /bus-service-management-system/src/main/resources/backup/create_table.sql
```

### セキュリティ設定例
- postgresql.conf：
```
listen_addresses = '*'
password_encryption = scram-sha-256
```
 
- pg_hba.conf：
```
host all app_user 10.0.0.0/24 scram-sha-256
```
- 設定反映の為再起動：
```
sudo systemctl restart postgresql
```
※ trust 認証は使用しない

※ 本検証環境ではSSL接続を使用していません。本番環境ではセキュリティを確保するため、**SSL接続を検討してください**。

※ サンプルはそのまま使用できません。環境や用途に応じて適宜変更してご利用ください。

---

## 4.3 アプリケーション設定

### ソースファイルの取得（例）
```bash
# 初回
git clone [REPOSITORY_URL]

# 2回目以降
git checkout [BRANCH_NAME]
git pull origin [BRANCH_NAME]
```
**補足**
- `[REPOSITORY_URL]`：ソースコード取得元リポジトリのクローンURL
- `[BRANCH_NAME]`：ソースコード取得対象のブランチ名
- Gitリポジトリを参照するための認証設定等は利用環境に合わせて適宜実施してください

### ソースファイルのコンパイル
```bash
cd [SRC_BASE]
sudo [MAVEN_PATH] clean package -P [PROFILE_ID]
```
**補足**
- `[SRC_BASE]`：ソースコード配置ディレクトリ。git clone等で事前に取得しておきます
- `[MAVEN_PATH]`：maven 配置ディレクトリ
- `[PROFILE_ID]`：`pom.xml` に定義されている Maven Profile ID
- 環境別（dev / stg / prod）で Profile を切り替えることを推奨します

---
### アプリケーション起動（例）

```bash
sudo java -jar target/bus-service-management-system.jar
```

または Profile 指定：

```bash
sudo java -jar target/bus-service-management-system.jar --spring.profiles.active=prod
```

## 4.4 バックアップ・レストアシェル設定

### バックアップ用フォルダ作成
```bash
sudo mkdir -p /opt/backup/scripts
sudo mkdir -p /opt/backup/postgres
sudo mkdir -p /opt/backup/log
```
### バックアップ用シェルスクリプト実行
バックアップ用のシェルスクリプト `output-db-backup-batch.sh` は、`src/main/resources/backup` フォルダに配置されています。  
/opt/backup/scripts/ に移行させ、実行してください。

※ 作成したデータベース名に合わせて、各スクリプト内の {DB_NAME} を修正してください。

バックアップ実行例：
```bash
sudo bash output-db-backup-batch.sh
```
dump生成例：
```bash
dumpfile_20260205190103.dump
```
エラーログ確認例：
```bash
cat /opt/backup/log/output-db-backup-batch.sh_YYYYMMDD.log
```

### レストア用シェルスクリプト実行
レストア用のシェルスクリプト `restore-db-batch.sh` は、`src/main/resources/backup` フォルダに配置されています。  
/opt/backup/scripts/ に移行させ、実行してください。

レストア実行例：
```bash
sudo bash restore-db-batch.sh /opt/backup/postgres/dumpfile_20251110061728.dump
```
エラーログ確認例：
```bash
cat /opt/backup/log/restore-db-batch_YYYYMMDD.log
```

### 推奨・注意事項：

- バックアップバッチは定期実行のため cronでの自動化 を推奨します。
- 現状は ローカル保存のみ のため、運用上は S3などの外部ストレージへの退避 + ライフサイクル設定 を追加検討してください。
- バックアップデータは 暗号化 を行うことを推奨します。
- 定期的に レストアテスト（整合性検証） を行い、バックアップの有効性を確認してください。
- バックアップ失敗時には通知が届く仕組みを用意することを推奨します。
---

# 5. バッチサーバ構築

## 5.1 アプリケーション設定

### ソースファイルの取得（例）
```bash
# 初回
git clone [REPOSITORY_URL]

# 2回目以降
git checkout [BRANCH_NAME]
git pull origin [BRANCH_NAME]
```
**補足**
- `[REPOSITORY_URL]`：ソースコード取得元リポジトリのクローンURL
- `[BRANCH_NAME]`：ソースコード取得対象のブランチ名
- Gitリポジトリを参照するための認証設定等は利用環境に合わせて適宜実施してください

### ソースファイルのコンパイル
```bash
cd [SRC_BASE]
sudo [MAVEN_PATH] clean package -P [PROFILE_ID]
```
**補足**
- `[SRC_BASE]`：ソースコード配置ディレクトリ。git clone等で事前に取得しておきます
- `[MAVEN_PATH]`：maven 配置ディレクトリ
- `[PROFILE_ID]`：`pom.xml` に定義されている Maven Profile ID
- 環境別（dev / stg / prod）で Profile を切り替えることを推奨します

---
### バッチ起動（例）

```bash
sudo java -jar target/output-gtfs-rt-batch-jar-with-dependencies.jar
```

または Profile 指定：

```bash
sudo java -jar target/output-gtfs-rt-batch-jar-with-dependencies.jar --spring.profiles.active=prod
```

**補足**

- ビルド成功後は、各バッチ処理を実行して成果物を確認してください。

    GTFS Realtime作成バッチの場合：

     ・ S3バケット内に事業者IDごとのフォルダが作成されているか確認

     ・ ファイルが正しくアップロードされているか確認

      ※ 対象レコードが存在しない場合はファイルは作成されません

    日毎便ダイヤ作成バッチの場合：

     ・ 日毎便ダイヤのテーブルを確認し、更新日が今日のデータが正しく作成されているか確認

- cronでの自動化を推奨します。
```bash
crontab -e
```

---

# 6. 運用・注意事項（検証向け）

- 本構成は **可用性を保証しない**
- 本番利用時は構成の見直しが必須
- 長期運用は禁止

---

# ライセンス

MIT License
