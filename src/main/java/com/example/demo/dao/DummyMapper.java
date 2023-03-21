package com.example.demo.dao;

import com.example.demo.dto.DummyDTO;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.mapping.ResultSetType;

@Mapper
public interface DummyMapper {


    @Insert("<script>\n"
      + "insert into dummy_data(data) values "
      + "<foreach item=\"item\" collection=\"list\" separator=\",\">\n"
      + "  (#{item.data})\n"
      + "</foreach>\n"
      + "</script>")
    int insertList(@Param("list") List<DummyDTO> dummyDTOS);

    @Select("select id,data from dummy_data where id>= #{id}")
    @Options(fetchSize = 1000, resultSetType = ResultSetType.FORWARD_ONLY)
    Cursor<DummyDTO> exportList(@Param("id") Long id);
}
