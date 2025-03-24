import { ref, watchEffect, toValue } from "vue";
import axios from "@/utils/request";
import { message } from "ant-design-vue";
// 获取列表
export function useTableList(url, data) {
  let list = ref([]);
  let total = ref(0);
  let loading = ref(false);

  watchEffect(() => {
    loading.value = true;
    axios
      .get(toValue(url), {
        params: toValue(data),
      })
      .then(({ data }) => {
        loading.value = false;
        if (data.code == 200) {
          list.value = data.rows;
          total.value = data.total;
        }
      }).catch(() => {
        loading.value = false;
      });
  });

  return { list, total, loading };
}

// 添加
export function useAdd(url, data) {
  let loading = ref(true);
  let success = ref(false);
  return axios.post(toValue(url), toValue(data)).then(({ data }) => {
    loading.value = false;
    if (data.code == 200) {
      success.value = true;
      message.success("提交成功");
      return Promise.resolve({ loading, success });
    }

  }).catch(() => {
    loading.value = false;
    success.value = false
    return Promise.resolve({ loading, success });
  });
  // return { loading, success}

}
// 删除
export function useDel(url, data) {
  let loading = ref(true);
  let success = ref(false);
  return axios
    .delete(toValue(url), {
      params: toValue(data),
    })
    .then(({ data }) => {
      loading.value = false;
      if (data.code == 200) {
        success.value = true;
        message.success("删除成功");
        return Promise.resolve({ loading, success });
      }
    });
}
//编辑
export function useEdit(url, data) {
  let loading = ref(true);
  let success = ref(false);
  return axios.post(toValue(url), toValue(data)).then(({ data }) => {
    loading.value = false;
    if (data.code == 200) {
      success.value = true;
      message.success("修改成功");
      return Promise.resolve({ loading, success });

    }
  });
}

// 详情
export function useGetDetail(url, data) {
  let detail = ref();
  let loading = ref(false);
  loading.value = true;

  return axios
    .get(toValue(url), {
      params: toValue(data),
    })
    .then(({ data }) => {
      loading.value = false;
      if (data.code == 200) {
        detail.value = data.data;
      }
      return Promise.resolve({ detail, loading });
    });
  // return { detail,loading};
}
