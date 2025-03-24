<template>
  <Header />
  <RouterView />
</template>
<script setup>
import { onMounted } from "vue";
import Header from "@/components/header/index.vue";
</script>
<style scoped></style>
