<script setup>
import { RouterLink, RouterView } from 'vue-router'
import Header from "@/components/Header.vue";
</script>

<template>
  <el-container>
    <el-header>
      <Header/>
    </el-header>
  </el-container>

  <RouterView />
</template>

<style scoped>

</style>
