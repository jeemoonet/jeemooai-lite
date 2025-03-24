<template>
  <div class="deepThinking">
    <div class="deepThinking-switch left" @click="onToggle">
      <div class="icon-ds">
        <svg
          width="20"
          height="20"
          viewBox="0 0 20 20"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
        >
          <path
            d="M2.656 17.344c-1.016-1.015-1.15-2.75-.313-4.925.325-.825.73-1.617 1.205-2.365L3.582 10l-.033-.054c-.5-.799-.91-1.596-1.206-2.365-.836-2.175-.703-3.91.313-4.926.56-.56 1.364-.86 2.335-.86 1.425 0 3.168.636 4.957 1.756l.053.034.053-.034c1.79-1.12 3.532-1.757 4.957-1.757.972 0 1.776.3 2.335.86 1.014 1.015 1.148 2.752.312 4.926a13.892 13.892 0 0 1-1.206 2.365l-.034.054.034.053c.5.8.91 1.596 1.205 2.365.837 2.175.704 3.911-.311 4.926-.56.56-1.364.861-2.335.861-1.425 0-3.168-.637-4.957-1.757L10 16.415l-.053.033c-1.79 1.12-3.532 1.757-4.957 1.757-.972 0-1.776-.3-2.335-.86zm13.631-4.399c-.187-.488-.429-.988-.71-1.492l-.075-.132-.092.12a22.075 22.075 0 0 1-3.968 3.968l-.12.093.132.074c1.308.734 2.559 1.162 3.556 1.162.563 0 1.006-.138 1.298-.43.3-.3.436-.774.428-1.346-.008-.575-.159-1.264-.449-2.017zm-6.345 1.65l.058.042.058-.042a19.881 19.881 0 0 0 4.551-4.537l.043-.058-.043-.058a20.123 20.123 0 0 0-2.093-2.458 19.732 19.732 0 0 0-2.458-2.08L10 5.364l-.058.042A19.883 19.883 0 0 0 5.39 9.942L5.348 10l.042.059c.631.874 1.332 1.695 2.094 2.457a19.74 19.74 0 0 0 2.458 2.08zm6.366-10.902c-.293-.293-.736-.431-1.298-.431-.998 0-2.248.429-3.556 1.163l-.132.074.12.092a21.938 21.938 0 0 1 3.968 3.968l.092.12.074-.132c.282-.504.524-1.004.711-1.492.29-.753.442-1.442.45-2.017.007-.572-.129-1.045-.429-1.345zM3.712 7.055c.202.514.44 1.013.712 1.493l.074.13.092-.119a21.94 21.94 0 0 1 3.968-3.968l.12-.092-.132-.074C7.238 3.69 5.987 3.262 4.99 3.262c-.563 0-1.006.138-1.298.43-.3.301-.436.774-.428 1.346.007.575.159 1.264.448 2.017zm0 5.89c-.29.753-.44 1.442-.448 2.017-.008.572.127 1.045.428 1.345.293.293.736.431 1.298.431.997 0 2.247-.428 3.556-1.162l.131-.074-.12-.093a21.94 21.94 0 0 1-3.967-3.968l-.093-.12-.074.132a11.712 11.712 0 0 0-.71 1.492z"
            fill="currentColor"
            stroke="currentColor"
            stroke-width=".1"
          ></path>
          <path
            d="M10.706 11.704A1.843 1.843 0 0 1 8.155 10a1.845 1.845 0 1 1 2.551 1.704z"
            fill="currentColor"
            stroke="currentColor"
            stroke-width=".2"
          ></path>
        </svg>
      </div>
      已深度思考（点击可{{ visible ? "收起" : "展开" }}）
      <div class="icon-switch" :class="visible ? '' : 'close'">
        <svg
          width="10"
          height="6"
          viewBox="0 0 10 6"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
        >
          <path
            d="M5.786 4.167L2.765 1.259c-.416-.4-.985-.482-1.273-.183-.287.298-.183.864.233 1.264l3.021 2.908c.416.4.986.482 1.273.184.287-.299.183-.865-.233-1.265z"
            fill="currentColor"
          ></path>
          <path
            d="M8.197 1.206L5.288 4.208c-.4.413-.484.982-.187 1.27.298.289.864.187 1.265-.227L9.274 2.25c.401-.414.485-.983.187-1.271-.297-.288-.863-.187-1.264.227z"
            fill="currentColor"
          ></path>
        </svg>
      </div>
    </div>
    <div class="deepThinking-content" :class="visible ? '' : 'close'">
      <div class="deepThinking-line"></div>
      <div
        class="deepThinking-text"
        v-for="(item, index) in splitReasoningContent"
        :key="index"
      >
        {{ item }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { DownOutlined } from "@ant-design/icons-vue";
import { ref, onMounted, inject, defineProps, defineEmits, computed } from "vue";
const props = defineProps({
  reasoningContent: {
    type: String,
    default: "",
  },
});
const visible = ref(true);

const onToggle = () => {
  visible.value = !visible.value;
};

// 使用 computed 处理 reasoningContent
const splitReasoningContent = computed(() => {
  return props.reasoningContent.split("\n").filter((line) => line.trim() !== "");
});
</script>

<style scoped lang="less">
.deepThinking {
  padding-left: 25px;
  padding-right: 18px;
  margin-bottom: 16px;
  .deepThinking-switch {
    width: fit-content;
    border-radius: 10px;
    font-size: 12px;
    line-height: 16px;
    font-weight: 500;
    color: #3d3d3d;
    padding: 7px 14px;
    user-select: none;
    background: rgb(245, 245, 245);
    cursor: pointer;
    &:hover {
      background: rgb(237, 237, 237);
    }
    .icon-ds {
      font-size: 12px;
      width: 12px;
      height: 12px;

      margin-right: 6px;
      svg {
        width: inherit;
        height: inherit;
        fill: none;
      }
    }
    .icon-switch {
      font-size: 10px;
      transform: rotate(0deg);
      margin-left: 6px;
      cursor: pointer;
      transition: all 0.15s ease-in-out;
      svg {
        width: inherit;
        height: inherit;
        fill: none;
      }
      &.close {
        transform: rotate(180deg);
      }
    }
  }
  .deepThinking-content {
    color: #8b8b8b;
    white-space: pre-wrap;
    margin: 0;
    padding: 0 0 0 13px;
    line-height: 26px;
    position: relative;
    margin-top: 12px;
    transition: all 0.15s ease-in-out;
    &.close {
      height: 0;
      overflow: hidden;
      margin-top: 0;
      //   display: none;
    }
    .deepThinking-line {
      border-left: 2px solid #e5e5e5;
      height: calc(100% - 10px);
      margin-top: 5px;
      position: absolute;
      top: 0;
      left: 0;
    }
    .deepThinking-text {
      font-size: 14px;
    }
  }
}
</style>
