package com.ngnis.walle.datasource.db.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "board")
public class BoardDO {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 创建时间
     */
    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @Column(name = "gmt_modified")
    private Date gmtModified;

    /**
     * 是否删除 0：否 1：是
     */
    @Column(name = "is_deleted")
    private Byte isDeleted;

    /**
     * 所属用户
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 模板编码
     */
    @Column(name = "board_code")
    private String boardCode;

    /**
     * 模板的名称
     */
    @Column(name = "board_name")
    private String boardName;

    /**
     * 消息类型 text link markdown
     */
    @Column(name = "message_type")
    private String messageType;

    /**
     * 消息标题
     */
    private String title;

    /**
     * link消息的跳转链接
     */
    @Column(name = "link_message_url")
    private String linkMessageUrl;

    /**
     * link消息的配图链接
     */
    @Column(name = "link_pic_url")
    private String linkPicUrl;

    /**
     * 艾特所有人 0：否 1：是
     * 只支持text和markdown类型的消息
     */
    @Column(name = "at_all")
    private Byte atAll;

    /**
     * 消息正文
     */
    private String content;

    /**
     * 消息的接收者列表，JSONArray格式
     */
    @Column(name = "address_list")
    private String addressList;
}