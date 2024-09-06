package com.xclhove.xnote.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xclhove.xnote.pojo.table.ShareNoteRecord;
import com.xclhove.xnote.pojo.vo.ShareNoteRecordVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author xclhove
 */
public interface ShareNoteRecordMapper extends BaseMapper<ShareNoteRecord> {
    
    @Select("select SNR.id, SNR.user_id, SNR.code, SNR.expire_time,SNR.note_id, N.title "
            + "from (select * from share_note_record where user_id = #{userId}) SNR, "
            + "(select id, title from note where user_id = #{userId}) N "
            + "where SNR.note_id = N.id "
            + "order by SNR.expire_time desc "
            + "${ew.customSqlSegment}"
    )
    Page<ShareNoteRecordVO> pageUserShareNote(
            @Param("userId")
            Integer userId,
            @Param("page")
            Page<ShareNoteRecordVO> page,
            @Param(Constants.WRAPPER)
            Wrapper<ShareNoteRecordVO> wrapper
    );
}
