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
 * @since 2021-11-26
 */
@Getter
@Setter
@TableName("sys_word")
@ApiModel(value = "Word对象", description = "")
public class Word implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("english")
    private String english;

    @TableField("chinese")
    private String chinese;

    @TableField("sent")
    private String sent;

    @TableField("level")
    private String level;


}
