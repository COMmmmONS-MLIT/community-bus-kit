package maint.mapper;

import maint.entity.CodeMasterEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * コードマスタMAPPER
 *
 * @author sangsoo.lee
 */
@Mapper
public interface CodeMasterMapper {

  /**
   * コードマスタ一覧取得
   *
   * @return コードマスタ一覧
   */
  List<CodeMasterEntity> getCodeMasterIndex();

}
