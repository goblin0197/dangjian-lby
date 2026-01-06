// package com.coder.springbootinit.utils.excel;

// import com.alibaba.excel.context.AnalysisContext;
// import com.alibaba.excel.event.AnalysisEventListener;
// import com.alibaba.excel.exception.ExcelDataConvertException;
// import com.coder.springbootinit.exception.BusinessException;
// import com.coder.springbootinit.model.dto.user.UserBatchAddRequest;
// import com.coder.springbootinit.service.UserService;
// import lombok.extern.slf4j.Slf4j;
// import org.apache.commons.lang3.StringUtils;

// import java.util.ArrayList;
// import java.util.List;

// /**
//  * 用户Excel导入监听器
//  *
// */
// @Slf4j
// public class UserExcelListener extends AnalysisEventListener<UserBatchAddRequest.UserImportItem> {

//     /**
//      * 批处理阈值
//      */
//     private static final int BATCH_COUNT = 100;

//     /**
//      * 用户列表
//      */
//     private List<UserBatchAddRequest.UserImportItem> userList = new ArrayList<>();

//     /**
//      * 用户服务
//      */
//     private final UserService userService;

//     /**
//      * 构造函数
//      *
//      * @param userService 用户服务
//      */
//     public UserExcelListener(UserService userService) {
//         this.userService = userService;
//     }

//     /**
//      * 每解析一行数据就会调用此方法
//      *
//      * @param item    解析的数据
//      * @param context 上下文
//      */
//     @Override
//     public void invoke(UserBatchAddRequest.UserImportItem item, AnalysisContext context) {
//         // 校验必填项
//         if (StringUtils.isBlank(item.getUserAccount())) {
//             throw new BusinessException(com.coder.springbootinit.common.ErrorCode.PARAMS_ERROR, "学号不能为空");
//         }
//         if (StringUtils.isBlank(item.getUserName())) {
//             throw new BusinessException(com.coder.springbootinit.common.ErrorCode.PARAMS_ERROR, "姓名不能为空");
//         }
        
//         // 添加到列表
//         userList.add(item);
        
//         // 达到阈值，批量处理
//         if (userList.size() >= BATCH_COUNT) {
//             saveData();
//             userList.clear();
//         }
//     }

//     /**
//      * 所有数据解析完成后调用此方法
//      *
//      * @param context 上下文
//      */
//     @Override
//     public void doAfterAllAnalysed(AnalysisContext context) {
//         // 处理剩余数据
//         if (!userList.isEmpty()) {
//             saveData();
//         }
//         log.info("Excel导入完成，共处理" + userList.size() + "条数据");
//     }

//     /**
//      * 处理数据
//      */
//     private void saveData() {
//         UserBatchAddRequest request = new UserBatchAddRequest();
//         request.setUserList(userList);
//         userService.batchAddUser(request);
//     }

//     /**
//      * 处理解析异常
//      *
//      * @param exception 异常
//      * @param context   上下文
//      */
//     @Override
//     public void onException(Exception exception, AnalysisContext context) {
//         log.error("解析失败，第" + (context.readRowHolder().getRowIndex() + 1) + "行数据异常", exception);
//         if (exception instanceof ExcelDataConvertException) {
//             ExcelDataConvertException convertException = (ExcelDataConvertException) exception;
//             log.error("第" + (convertException.getRowIndex() + 1) + "行，第" + (convertException.getColumnIndex() + 1) + "列数据异常", exception);
//             throw new BusinessException(com.coder.springbootinit.common.ErrorCode.PARAMS_ERROR, "数据格式异常，请检查");
//         } else if (exception instanceof BusinessException) {
//             throw (BusinessException) exception;
//         } else {
//             throw new BusinessException(com.coder.springbootinit.common.ErrorCode.PARAMS_ERROR, "Excel解析失败");
//         }
//     }
// }
