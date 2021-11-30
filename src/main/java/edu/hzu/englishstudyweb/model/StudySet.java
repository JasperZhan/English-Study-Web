package edu.hzu.englishstudyweb.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author Jasper Zhan
 * @since 2021-11-26
 */
@Getter
@Setter
@Data
@TableName("sys_study_set")
@ApiModel(value = "StudySet对象", description = "")
public class StudySet implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("用户id")
    @TableField("user_id")
    private Integer user_id;

    @ApiModelProperty("单词id")
    @TableField("word_id")
    private Integer word_id;

    @ApiModelProperty("单词学习次数")
    @TableField("word_count")
    private Integer word_count;

    @ApiModelProperty("单词学习阶段（1 忘记， 2 模糊， 3 认识）")
    @TableField("word_status")
    private Integer word_status;


}
