
# Environment_Guide（AWS + サーバ統合版 / 検証・実証向け最小構成）

## 本ドキュメントについて（重要）

本ガイドは **実証（PoC / 検証）を目的とした最小構成** を前提としています。  
可用性・冗長性・高度なセキュリティ対策を施した **本番運用構成ではありません**。

- 本番利用時は、構成・インスタンスタイプ・ネットワーク設計・認証方式を
  **利用者自身の要件に応じて必ず見直してください**
- 本ドキュメントは **OSS（MIT License）として公開することを前提** にしています

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

### Amazon S3（成果物保存）

| 項目 | 設定 |
|---|---|
| パブリックアクセスブロック | **有効（ON）** |
| バージョニング | 有効（推奨） |
| 暗号化 | SSE-S3 |

※ 検証用途でも **公開バケットは作成しない**

---

## 1.5 GPSトラッカー受信（Lambda）

- Runtime：Java 17
- 実行方式：Function URL
- 認証方式：AWS_IAM（推奨）

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

**修正理由**
- DB接続先、S3バケット名、メール送信元を切り替えるため

---

## 3.2 S3 バケット名

```xml
<s3.bucket.name>xxxx-oss-bus</s3.bucket.name>
```

**修正理由**
- S3バケット名は AWS全体で一意
- OSSのサンプル値はそのまま使用できない

---

## 3.3 SES 送信元メールアドレス

```xml
<aws.ses.from.email>noreply@example.com</aws.ses.from.email>
```

**修正理由**
- SESでは送信元の検証が必須
- 未検証アドレスでは送信不可

---

## 3.4 AWS リージョン（必要に応じて）

```xml
<aws.region>ap-northeast-1</aws.region>
```

---

## 3.5 ビルド成果物名（任意）

```xml
<finalName>bus-service-management-system</finalName>
```

---

### 記載しない設定について（重要）

以下は **pom.xml に記載しません**。

- AWS Access Key / Secret Key
- DBパスワード
- APIトークン

※ SSM Parameter Store / Secrets Manager の利用を前提とします

---

# 4. WEBサーバ構築（検証向け）

## 4.1 Redis

```bash
sudo dnf install -y redis6
sudo systemctl enable --now redis6
```

---

## 4.2 PostgreSQL（安全な最小構成）

```bash
sudo dnf install -y postgresql16-server
```

### セキュリティ設定

- password_encryption = scram-sha-256
- pg_hba.conf：

```
hostssl all app_user 10.0.0.0/24 scram-sha-256
```

※ trust 認証は使用しない

---

# 5. バッチサーバ構築

- Java JARをcronで定期実行
- 成果物はS3へ出力

---

# 6. 運用・注意事項（検証向け）

- 本構成は **可用性を保証しない**
- 本番利用時は構成の見直しが必須
- 長期運用は禁止

---

# ライセンス

MIT License
