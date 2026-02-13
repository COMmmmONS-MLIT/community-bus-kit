#!/bin/bash

BACKUP_DIR="/opt/backup/postgres"
LOG_DIR="/opt/backup/log"
DB_NAME="mlit_community_bus_kit"
DATE=$(date '+%Y%m%d%H%M%S')
LOG_DATE=$(date '+%Y%m%d')
FILE_NAME="dumpfile_${DATE}.dump"
ERROR_LOG="${LOG_DIR}/output-db-backup-batch_${LOG_DATE}.log"

# バックアップ実行（エラーのみログに出力）
pg_dump -U postgres -Fc -b -v -f "${BACKUP_DIR}/${FILE_NAME}" $DB_NAME 2>> "$ERROR_LOG"

# 30日以上前のバックアップファイルを自動削除
find $BACKUP_DIR -type f -name "dumpfile_*.dump" -mtime +30 -exec rm -f {} \;

