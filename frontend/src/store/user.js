import {reactive} from 'vue'

/*
* 用户信息
* */
export default reactive({
    //是否登录
    login:false,
    //登录后的个人信息数据
    info:{}
})