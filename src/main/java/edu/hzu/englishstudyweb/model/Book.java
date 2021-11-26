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
@TableName("sys_book")
@ApiModel(value = "Book对象", description = "")
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("词书id")
    @TableId(value = "book_id", type = IdType.AUTO)
    private Integer bookId;

    @ApiModelProperty("词书名字")
    @TableField("book_name")
    private String bookName;

    @ApiModelProperty("词书描述")
    @TableField("description")
    private String description;


}
