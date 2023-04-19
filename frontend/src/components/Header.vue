<script setup>
import user from '@/store/user'

</script>

<template>
  <nav>
    <RouterLink to="/">
      <img alt = "SH logo" src = "@/assets/logo.svg" wdith = "40" height = "40">
      搜房网
    </RouterLink>
    <RouterLink to="/person" v-if="user.login" class="login-btn">{{ user.info.name }}</RouterLink>
    <RouterLink to="/login"  v-else class="login-btn">登录</RouterLink>
  </nav>
</template>

<style>
.login-btn{
  float:right;
}
</style>