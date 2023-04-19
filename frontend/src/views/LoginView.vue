<script setup>
import {reactive} from "vue";
import {getUserInfo, login,loginWithSmsCode} from "@/utils/http";
import router from "@/router";
import user from "@/store/user";
import {ElMessage} from "element-plus";

const data = reactive({
  form:{
    username:'',
    password:'',
  },
  smsForm: {
    phone: '',
    code: '',
  },
  activeTab: 'usernamePassword',
  smsCodeCountdown: 0,
});


async function sendSmsCode() {
  try {
    // 调用后端接口发送验证码
    const response = await fetch(`/api/users/sms?phoneNumber=${data.smsForm.phone}`, {
      method: 'POST',
    });

    // 判断响应状态
    if (response.ok) {
      // 发送成功，开始倒计时
      data.smsCodeCountdown = 60;
      const timer = setInterval(() => {
        data.smsCodeCountdown -= 1;
        if (data.smsCodeCountdown <= 0) {
          clearInterval(timer);
        }
      }, 1000);
    } else {
      // 发送失败，显示提示信息
      ElMessage({
        message: '验证码发送失败',
        type: 'error',
        showClose: true,
      });
    }
  } catch (error) {
    // 发送失败，显示提示信息
    ElMessage({
      message: '验证码发送失败',
      type: 'error',
      showClose: true,
    });
  }
}

function onSmsSubmit() {
  // 处理手机号验证码登录
  loginWithSmsCode(data.smsForm)
      .then((resp) => resp.json())
      .then((resp) => {
        if (resp.code === 0) {
          getUserInfo()
              .then((resp) => resp.json())
              .then((resp) => {
                if (resp.code === 0) {
                  user.login = true
                  user.info = resp.data
                } else {
                  ElMessage({
                    message: '发生了一些未知错误',
                    showClose: true,
                    type: 'error',
                })
                }
              })
          router.push('/')
        } else if(resp.code == 106){
          ElMessage({
            message: '登录失败，请检查手机号和验证码',
            type: 'error',
            showClose: true,
          })
        }else if(resp.code == 1){
          ElMessage({
            message: "发生了一些未知错误",
            showClose: "true",
            type: "error",
          })
        }
      })
}

function onSubmit(){
  login(data.form).then(resp => resp.json()).then(resp => {
    if(resp.code == 0){
      getUserInfo().then(resp => resp.json()).then(resp => {
        if(resp.code == 0){
          user.login = true
          user.info = resp.data
        }else{
          ElMessage({
            message: "发生了一些未知错误",
            showClose: "true",
            type: "error",
          })
        }
      })
      router.push('/')
    }else if(resp.code == 105){
      ElMessage({
        message:"用户名或密码错误",
        showClose: "true",
        type: "error",
      })
    }else if(resp.code == 1){
      ElMessage({
        message: "发生了一些未知错误",
        showClose: "true",
        type: "error",
      })
    }
  })
}
</script>
<template>
  <el-tabs v-model= "data.activeTab" type="border-card">
    <el-tab-pane label="用户名密码登录" name="usernamePassword">
      <el-form :model="data.form" label-width="120px">
        <el-form-item label="用户名:">
          <el-input v-model="data.form.username" placeholder="用户名\手机号"></el-input>
        </el-form-item>
        <el-form-item label="密码：">
          <el-input v-model="data.form.password" placeholder="密码"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit()">登录</el-button>
        </el-form-item>
      </el-form>
    </el-tab-pane>
    <el-tab-pane label="手机号验证码登录" name="smsCode">
      <el-form :model="data.smsForm" label-width="120px">
        <el-form-item label="手机号：">
          <el-input v-model="data.smsForm.phone" placeholder="手机号"></el-input>
        </el-form-item>
        <el-form-item label="验证码：">
          <el-input v-model="data.smsForm.code" placeholder="验证码">
            <template #append>
              <el-button
                  @click="sendSmsCode()"
                  :disabled="data.smsCodeCountdown > 0"
              >发送验证码{{ data.smsCodeCountdown > 0 ? `(${data.smsCodeCountdown})` : '' }}</el-button>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSmsSubmit()">登录</el-button>
        </el-form-item>
      </el-form>
    </el-tab-pane>
  </el-tabs>
</template>