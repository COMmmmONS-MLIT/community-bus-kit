#!/bin/bash

DB_NAME="mlit_community_bus_kit"
DB_USER="postgres"
PORT="5432"
LOG_DIR="/opt/backup/log"
LOG_DATE=$(date '+%Y%m%d')
ERROR_LOG="${LOG_DIR}/restore-db-batch_${LOG_DATE}.log"

DUMP_FILE="$1"

# ダンプファイルの存在確認
if [ ! -f "$DUMP_FILE" ]; then
  echo " バックアップファイルが見つかりません: $DUMP_FILE"
  exit 1
fi

# レストア開始
echo "レストアを開始します：$DUMP_FILE → $DB_NAME"
pg_restore --clean --if-exists -p $PORT -U $DB_USER -d $DB_NAME -v "$DUMP_FILE" >> "$ERROR_LOG" 2>&1
RESULT=$?

# 結果判定
if [ $RESULT -eq 0 ]; then
  echo "レストア完了しました。"
else
  echo "レストアに失敗しました。詳細は $ERROR_LOG を確認してください。"
fi

