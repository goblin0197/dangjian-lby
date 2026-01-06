// package com.coder.springbootinit.model.dto.user;

// import java.io.Serializable;
// import java.util.List;

// import com.alibaba.excel.annotation.ExcelProperty;
// import io.swagger.annotations.ApiModelProperty;
// import lombok.Data;

// /**
//  * 批量导入用户请求
//  *
// */
// @Data
// public class UserBatchAddRequest implements Serializable {

//     /**
//      * 用户列表
//      */
//     @ApiModelProperty(value = "用户列表", required = true)
//     private List<UserImportItem> userList;

//     /**
//      * 用户导入项
//      */
//     @Data
//     public static class UserImportItem implements Serializable {

//         /**
//          * 序号
//          */
//         @ExcelProperty(value = "序号", index = 0)
//         private Integer serialNumber;

//         /**
//          * 学号
//          */
//         @ExcelProperty(value = "学号", index = 1)
//         @ApiModelProperty(value = "学号", required = true)
//         private String userAccount; // 学号作为账号

//         /**
//          * 姓名
//          */
//         @ExcelProperty(value = "姓名", index = 2)
//         @ApiModelProperty(value = "姓名", required = true)
//         private String userName;

//         /**
//          * 密码
//          */
//         @ExcelProperty(value = "密码", index = 3)
//         private String userPassword;

//         /**
//          * 政治面貌
//          */
//         @ExcelProperty(value = "政治面貌", index = 4)
//         private String politicalStatus;

//         /**
//          * 类型
//          */
//         @ExcelProperty(value = "类型", index = 5)
//         private String userType;

//         /**
//          * 角色
//          */
//         @ExcelProperty(value = "角色", index = 6)
//         private String userRole;

//         private static final long serialVersionUID = 1L;
//     }

//     private static final long serialVersionUID = 1L;
// }