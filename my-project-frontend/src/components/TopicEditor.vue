<script setup>
defineProps({
  show:Boolean
})
</script>

<template>
<div>
  <el-drawer :model-value="show"
             direction="btt"
             :close-on-click-modal="false"
             :size="650"
  >
    <template #header>
      <div>
        <div style="font-weight: bold">发表新的帖子</div>
        <div style="font-size: 13px">发表帖子时，请遵守相关法律，不要出现骂人等不文明行为</div>
      </div>
    </template>
    <div style="display: flex;gap: 10px">
      <div style="width: 120px">
        <el-select placeholder="请选择类型">
          <el-option>测试</el-option>
        </el-select>
      </div>
      <div style="flex: 1">
        <el-input>
        </el-input>
      </div>
    </div>
  </el-drawer>
</div>
</template>

<style scoped>
:deep(.el-drawer){
  width: 800px;
  margin: auto;
  border-radius: 10px 10px 0 0;
}
:deep(.el-drawer__header){
  margin: 0;
}
</style>