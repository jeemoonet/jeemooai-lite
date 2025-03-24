<template>
  <div class="add-button">
    <div class="add-button-content iconcenter" @click="handleAdd">
      <img class="add-svg" src="@/assets/images/ico_add.png" />
      <!-- <slot class="add-svg" name="icon"></slot> -->
      {{ title }}
    </div>
  </div>
</template>
<script setup>
import { ref, reactive, defineProps, watch, defineEmits } from "vue";
const emit = defineEmits(["addClick"]);
const props = defineProps({
  title: {
    type: String,
    default: () => "添加文本",
  },
});

const handleAdd = () => {
  emit("addClick");
};
</script>
<style lang="less" scoped>
.add-button {
  width: 100%;
  padding: 0 0 0 30px;
  margin-bottom: 25px;
  .add-button-content {
    width: 100%;
    height: 44px;
    border-radius: 6px;
    text-align: center;
    align-items: center;
    cursor: pointer;
    color: #fff;
    font-weight: 500;
    font-size: 16px;
    background: #5657f6;
    transition: all linear 0.2s;
  }
  .add-svg {
    display: block;
    width: 18px;
    font-size: 16px;
    font-weight: 500;
    background-color: transparent;
    margin-right: 5px;
  }
}
</style>
