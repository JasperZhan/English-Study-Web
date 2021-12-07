package edu.hzu.englishstudyweb.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@TableName("sys_review_set")
@ApiModel(value = "ReviewSet对象", description = "")
public class ReviewSet implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("用户id")
    @TableField("user_id")
    private Integer userId;

    @ApiModelProperty("单词id")
    @TableField("word_id")
    private Integer wordId;

    @ApiModelProperty("单词复习中 认识的次数")
    @TableField("know_count")
    private Integer knowCount;

    @ApiModelProperty("单词复习中 模糊的次数")
    @TableField("vague_count")
    private Integer vagueCount;

    @ApiModelProperty("单词复习中 忘记的次数")
    @TableField("forget_count")
    private Integer forgetCount;

    @ApiModelProperty("学习该单词的日期（添加到复习单词表的日期）")
    @TableField("study_date")
    private LocalDate studyDate;

    @ApiModelProperty("下一次学习日期间隔")
    @TableField("date_interval")
    private Integer dateInterval;

    @ApiModelProperty("当前复习状态")
    @TableField("current_status")
    private Integer currentStatus;


}
