import { ref, reactive, computed } from 'vue'
import { defineStore } from 'pinia'

export const WorkflowStore = defineStore('workflow', () => {
  const collapsed =ref(false)

  const workflowUpdateData = reactive({
    hash: "",
    update_time: ""
  });
  const getWorkflowUpdateData = computed(() => workflowUpdateData)
  const setWorkflowUpdateData = (res = {}) => {
    workflowUpdateData.hash = res.hash || "";
    if (res.update_time) {
      workflowUpdateData.update_time = res.update_time;
    }
  }
  const setCollapsed = (val)=>{
    collapsed.value = val
  }


  return { workflowUpdateData, getWorkflowUpdateData, setWorkflowUpdateData ,collapsed,setCollapsed}
})
