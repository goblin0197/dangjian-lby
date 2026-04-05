// 统一导入更新后的权限枚举（注意：原代码重复导入，需删除重复项）
import ACCESS_ENUM from "@/access/accessEnum";

/**
 * 检查权限，判断当前登录用户是否具有某个权限
 * @param loginUser 当前登录用户（若未登录则为 undefined/null）
 * @param needAccess 需要的权限（默认值：NOT_LOGIN）
 * @return boolean 有无权限
 */
const checkAccess = (
  loginUser: { userRole?: string } | undefined | null,
  needAccess: string = ACCESS_ENUM.NOT_LOGIN
): boolean => {
  // 1. 兜底处理：如果传入的 needAccess 无效，默认按 NOT_LOGIN 处理
  const validAccessList = Object.values(ACCESS_ENUM);
  if (!validAccessList.includes(needAccess)) {
    console.warn(`无效的权限类型：${needAccess}，默认按 NOT_LOGIN 处理`);
    needAccess = ACCESS_ENUM.NOT_LOGIN;
  }

  // 2. 获取当前登录用户的权限（未登录则为 NOT_LOGIN）
  const loginUserAccess = loginUser?.userRole ?? ACCESS_ENUM.NOT_LOGIN;

  // 3. 无需登录的权限：直接返回 true（所有人可访问）
  if (needAccess === ACCESS_ENUM.NOT_LOGIN) {
    return true;
  }

  // 4. 需要登录的权限：先判断用户是否未登录，未登录则直接无权限
  if (loginUserAccess === ACCESS_ENUM.NOT_LOGIN) {
    return false;
  }

  // 5. 超级管理员拥有所有权限（兜底逻辑）
  if (loginUserAccess === ACCESS_ENUM.SUPER_ADMIN_ROLE) {
    return true;
  }

  // 6. 普通角色：精确匹配所需权限
  return loginUserAccess === needAccess;
};

export default checkAccess;
