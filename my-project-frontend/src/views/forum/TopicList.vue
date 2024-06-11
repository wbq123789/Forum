<script setup>

import LightCard from "@/components/LightCard.vue";
import {Calendar, CollectionTag, EditPen, Link} from "@element-plus/icons-vue";
import Weather from "@/components/Weather.vue";
import {computed, reactive, ref} from "vue";
import {get} from "@/net";
import TopicEditor from "@/components/TopicEditor.vue";

const today=computed(()=>{
  const data= new Date()
  return `${data.getFullYear()} 年 ${data.getMonth()} 月 ${data.getDay()} 日`
})
const IP=ref("127.0.0.1")
const weather=reactive({
  location:{},
  now:{},
  hourly:[],
  success:false
});
const editor=ref(false)
get('api/forum/getIP',(data)=>{
  IP.value=data
})
navigator.geolocation.getCurrentPosition(position => {
  const longitude = position.coords.longitude
  const latitude = position.coords.latitude
  position.coords
  get(`api/forum/weather?longitude=${longitude}&latitude=${latitude}`,data=>{
    Object.assign(weather,data)
    weather.success =true
  })
},error=>{
  console.info(error)
  console.log("地理位置信息获取失败，请检查浏览器设置（注意：chrome浏览器存在获取失败的问题，请更换其他浏览器再次尝试！）")
  get(`api/forum/weather?longitude=116.40529&latitude=39.90499`,data=>{
  Object.assign(weather,data)
  weather.success =true
})
},{
  timeout:10000,
  enableHighAccuracy:true
})
</script>

<template>
<div style="display: flex;margin: 20px auto;gap: 20px;max-width: 900px">
  <div style="flex: 1">
    <LightCard>
      <div class="create-topic" @click="editor=true">
        <el-icon class="icon"><EditPen/></el-icon>
        点击发表主题...
      </div>
    </LightCard>
    <light-card style="margin-top: 10px;height: 50px">

    </light-card>
    <div style="margin-top: 10px;display: flex;flex-direction: column;gap: 10px">
      <light-card style="height: 150px" v-for="item in 10">

      </light-card>
    </div>
  </div>
  <div style="width: 300px">
    <div style="position: sticky;top: 20px">
      <LightCard>
        <div style="font-weight: bold;">
          <el-icon class="icon">
            <CollectionTag/>
          </el-icon>
          论坛公告栏
        </div>
        <el-divider style="margin: 10px 0"/>
        <div style="font-size: 14px;margin: 10px;color: grey">
          你可以在这里与他人畅聊技术，分享经验，结识更多优秀的小伙伴。
        </div>
      </LightCard>
      <LightCard style="margin-top: 10px">
        <div style="font-weight: bold;">
          <el-icon class="icon">
            <Calendar/>
          </el-icon>
          今日天气
        </div>
        <el-divider style="margin: 10px 0"/>
        <Weather :data="weather"/>
      </LightCard>
      <light-card>
        <div class="info-text">
          <div>当前日期</div>
          <div>{{today}}</div>
        </div>
        <div class="info-text">
          <div>当前IP</div>
          <div>{{IP}}</div>
        </div>
      </light-card>
      <div style="font-size: 14px;margin-top: 10px;color: grey">
        <el-icon class="icon"><Link/></el-icon>
        友情链接 暂无
        <el-divider style="margin: 10px 0"/>
      </div>
    </div>
  </div>
  <TopicEditor :show="editor" @close="editor=false"></TopicEditor>
</div>
</template>

<style lang="less" scoped>
.info-text{
  display: flex;
  justify-content: space-between;
  font-size: 14px;
  color: grey;
}
.create-topic{
  background-color: #e4e4e4;
  border-radius: 5px;
  height: 40px;
  font-size: 14px;
  line-height: 40px;
  padding: 0 10px;
  color: grey;
}
.dark .create-topic{
  background-color: #454545;
  color: #939393;
}
.icon{
  margin-right: 3px;
  translate: 0 2px
}
&:hover{
  cursor: pointer;
}
</style>