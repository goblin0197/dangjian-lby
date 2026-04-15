module.exports = {
    root: true,
    env: {
        node: true,
    },
    extends: [
        "plugin:vue/vue3-essential",
        "eslint:recommended",
        "@vue/typescript/recommended",
    ],
    parserOptions: {
        ecmaVersion: 2020,
    },
    rules: {
        "no-console": process.env.NODE_ENV === "production" ? "warn" : "off",
        "no-debugger": process.env.NODE_ENV === "production" ? "warn" : "off",
        // 允许对象/数组末尾逗号（避免解析器误判）
        "comma-dangle": ["error", "only-multiline"],
        // 关闭不必要的语法解析严格校验
        "@typescript-eslint/comma-dangle": ["error", "only-multiline"],
    },
};
