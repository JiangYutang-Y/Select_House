
/*
* 登录
* */
export async function login(form) {
    let d = new FormData()
    d.set("username",form.username)
    d.set("password",form.password)
    let rsp = await fetch('/api/sessions',{
        method:'POST',
        credentials:'include',
        body:d,
    })
    console.log(rsp)
    return rsp
}
/*
* 获取用户信息
* */
export async function getUserInfo(){
    return fetch('/api/users/info',{
        method:'Get',
        header:{
            'content-Type':'application/json',
            'Accept':'application/json'
        },
        credentials:'include'
    })
}

export async function loginWithSmsCode(form) {
    let d = new FormData()
    d.set("phone",form.phone)
    d.set("code",form.code)
    let rsp = await fetch('/api/sessionsSms?method=sms', {
        method:'POST',
        credentials:'include',
        body:d,
    })
    console.log(rsp)
    return rsp
}
