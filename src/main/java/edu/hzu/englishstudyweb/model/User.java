package edu.hzu.englishstudyweb.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
 * @since 2021-11-27
 */
@Getter
@Setter
@TableName("sys_user")
@ApiModel(value = "User对象", description = "")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户表的主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("用户的手机号，作为登录账号时使用")
    @TableField("tell")
    private String tell;

    @ApiModelProperty("用户的密码，作为登录账号时使用")
    @TableField("password")
    private String password;

    @ApiModelProperty("用户选择的词书，保存词书的id")
    @TableField("book_id")
    private Integer bookId;

    @ApiModelProperty("用户的学习单词集id")
    @TableField("study_set_id")
    private Integer studySetId;

    @ApiModelProperty("用户的复习单词集id")
    @TableField("review_set_id")
    private Integer reviewSetId;

    @ApiModelProperty("用户单词收藏夹id")
    @TableField("collection_id")
    private Integer collectionId;


}
