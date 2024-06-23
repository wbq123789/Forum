<script setup>

import {useRoute} from "vue-router";
import {get, post} from "@/net";
import axios from "axios";
import {computed, reactive, ref} from "vue";
import { QuillDeltaToHtmlConverter } from 'quill-delta-to-html';
import {ArrowLeft, CircleCheck, EditPen, Female, Male, Star} from "@element-plus/icons-vue";
import Card from "@/components/Card.vue";
import router from "@/router";
import TopicTag from "@/components/TopicTag.vue";
import InteractButton from "@/components/InteractButton.vue";
import {ElMessage} from "element-plus";
import {useStore} from "@/store";
import TopicEditor from "@/components/TopicEditor.vue";

const route=useRoute()
const topic=reactive({
  data:null,
  like:false,
  collect:false,
  comments:[]
})
const edit=ref(false)
const store=useStore()
const tid=route.params.tid;
const init = () => get(`api/forum/topic?tid=${tid}`,data=> {
  topic.data = data
  topic.like=data.interact.like
  topic.collect=data.interact.collect
})
init()
const content=computed(()=>{
  const ops = JSON.parse(topic.data.content).ops
  const converter = new QuillDeltaToHtmlConverter(ops, { inlineStyles: true });
  return converter.convert();
})
function interact(type,message){
  get(`/api/forum/interact?tid=${tid}&type=${type}&state=${!topic[type]}`,()=>{
    topic[type]= !topic[type]
    if(topic[type])
      ElMessage.success(`${message}成功！`)
    else
      ElMessage.success(`已取消${message}！`)
  })
}

function updateTopic(editor) {
  post('/api/forum/update-topic', {
    id: tid,
    type: editor.type.id,
    title: editor.title,
    content: editor.text
  }, () => {
    ElMessage.success('帖子内容更新成功！')
    edit.value = false
    init()
  })
}
</script>

<template>
  <div class="topic-page" v-if="topic.data">
    <div class="topic-main" style="position: sticky;top:0;z-index: 10">
      <card style="display: flex;width: 100%">
        <el-button :icon="ArrowLeft" type="info" size="small"
        plain round @click="router.push('/index')">返回列表</el-button>
        <div style="text-align: center;flex: 1">
          <TopicTag :type="topic.data.type" />
          <span style="font-weight: bold;margin-left: 5px">{{topic.data.title}}</span>
        </div>
      </card>
    </div>
    <div class="topic-main">
      <div class="topic-main-left">
        <el-avatar :src="topic.data.user.avatar===null ?
        'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png':
        `${axios.defaults.baseURL}/api/file/download/images${topic.data.user.avatar}`"
        :size="60"/>
        <div>
          <div style="font-size: 18px;font-weight: bold">
            {{topic.data.user.username}}
            <span style="color: hotpink" v-if="topic.data.user.gender ===1">
              <el-icon><Female/></el-icon>
            </span>
            <span style="color: dodgerblue" v-if="topic.data.user.gender ===0">
              <el-icon><Male/></el-icon>
            </span>
          </div>
          <div class="desc">{{topic.data.user.email}}</div>
        </div>
        <el-divider style="margin: 10px 0"/>
        <div style="text-align: left;margin: 0 5px">
          <div class="desc">微信号: {{topic.data.user.wx || '已隐藏或未填写'}}</div>
          <div class="desc">QQ号: {{topic.data.user.qq || '已隐藏或未填写'}}</div>
          <div class="desc">手机号: {{topic.data.user.phone || '已隐藏或未填写'}}</div>
        </div>
        <el-divider style="margin: 10px 0"/>
        <div class="desc" style="margin: 0 5px">{{topic.data.user.intro||'这个用户很神秘，没有个人简介！'}}</div>
      </div>
      <div class="topic-main-right">
        <div class="topic-content" v-html="content"></div>
        <el-divider/>
        <div style="font-size: 13px;color: grey;text-align: center">
          <div>发帖时间：{{new Date(topic.data.time).toLocaleString()}}</div>
        </div>
        <div style="text-align: right;margin-top: 30px">
          <interact-button name="编辑帖子" color="dodgerblue"
                           :check="false"
                           @check="edit=true"
                           style="margin-right: 20px"
                           v-if="store.user.id === topic.data.user.id"
          >
            <el-icon style="transform: translateY(2px)"><EditPen/></el-icon>
          </interact-button>
          <interact-button check-name="已点赞" name="点个赞吧" color="pink"
                           @check="interact('like','点赞')"
                           :check="topic.like"
          >
            <el-icon style="transform: translateY(2px)"><CircleCheck/></el-icon>
          </interact-button>
          <interact-button check-name="已收藏" name="收藏一下" color="orange"
                           @check="interact('collect','收藏')"
                           :check="topic.collect"
                           style="margin-left: 20px">
            <el-icon style="transform: translateY(2px)"><Star/></el-icon>
          </interact-button>
        </div>
      </div>
    </div>
    <topic-editor :show="edit" @close="edit=false" v-if="topic.data && store.forum.types"
    :default-title="topic.data.title" :default-type="topic.data.type"
    :default-text="topic.data.content" submit-button="更新帖子内容" :submit="updateTopic"/>
  </div>
</template>

<style scoped>
.topic-page{
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding: 10px 0;
}
.topic-main{
  display: flex;
  border-radius: 7px;
  margin: 0 auto;
  background-color: var(--el-bg-color);
  width: 800px;
  .topic-main-left{
    width: 210px;
    padding: 10px;
    text-align: center;
    border-right: solid 1px var(--el-border-color);

    .desc{
      font-size: 12px;
      color: grey;
    }
  }
  .topic-main-right{
    width: 600px;
    padding: 10px 20px;
    display: flex;
    flex-direction: column;

    .topic-content{
      font-size: 14px;
      line-height: 22px;
      opacity: 0.8;
      flex: 1;
    }
  }
}
</style>