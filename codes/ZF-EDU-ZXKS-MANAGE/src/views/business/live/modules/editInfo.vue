<template>
    <div class="live-edit-info">
        <a-card :bordered="false" class="live-add">
            <a-form-model
                ref="form"
                :model="form"
                :rules="rules"
                :label-col="labelCol"
                :wrapper-col="wrapperCol"
                >
                <a-row style="padding-bottom:50px;">
                    <a-col :span="24" :pull="4">
                        <a-form-model-item label="直播名称" prop="title">
                            <a-input placeholder="请输入直播名称" v-model="form.title"  style="width:50%"/>
                        </a-form-model-item>
                    </a-col>
                    <a-col :span="24" :pull="4">
                        <a-form-model-item label="直播分类" prop="type">
                            <a-select style="width: 50%" v-model="form.type" allowClear placeholder="请选择">
                                <a-select-option v-for="(item,index) in liveTypeOptions" :key="item.dictValue" :value="item.dictValue">
                                    {{item.dictLabel}}
                                </a-select-option>
                            </a-select>
                        </a-form-model-item>
                    </a-col>
                    <a-col :span="24" :pull="4">
                        <a-form-model-item label="直播延迟" prop="delay">
                            <a-radio-group v-model="form.delay" default-value="N" button-style="solid">
                                <a-radio-button value="N" style="height:60px;width:150px;">
                                    <div class="live-yc"><a-icon :component="allIcon['timeRangeIcon']"/> 正常延迟≈3s</div>
                                </a-radio-button>
                                <a-radio-button value="Y" class="live-yc" style="margin-left:20px;height:60px;width:150px;">
                                    <div class="live-yc"><a-icon type="rocket" style="font-size:16px;"/> 无延迟&lt;0.4s</div>
                                </a-radio-button>
                            </a-radio-group>
                            <a-icon type="info-circle" style="margin-left:20px;color:#1890ff"/>
                            <a-popover  trigger="focus" style="margin-left:-10px;">
                                <template slot="content">
                                    <p>开播：支持网页开播与win版客户端4.0及以上版本</p>
                                    <p>观看：支持Chrome、微信、iOS13及以上Safari浏览器</p>
                                </template>
                                <a-button type="link">
                                    无延迟直播
                                </a-button>
                            </a-popover>
                            </br>
                            <a-icon type="info-circle" /> 无延迟直播延迟低于0.4s（大直播正常延迟～3s），请在讲师登录前切换，直播过程中不允许切换
                        </a-form-model-item>
                    </a-col>
                    <a-col :span="24" :pull="4" >
                        <a-form-model-item label="直播时间" prop="liveStartTime">
                            <a-date-picker
                                v-model="form.liveStartTime"
                                valueFormat="YYYY-MM-DD HH:mm:ss"
                                :disabled-date="disabledDate"
                                :show-time="{ defaultValue: moment('00:00:00', 'HH:mm:ss') }"
                                style="width:50%"
                            />
                            </br>
                            <a-icon type="info-circle" /> 保存直播时间时，距设置的直播时间大于25分钟才可保存
                        </a-form-model-item>
                    </a-col>
                    <a-col :span="24" :pull="4">
                        <a-form-model-item label="直播封面">
                            <a-upload
                                :action="getUploadUrl"
                                style="margin:0px 0% 0% 0%"
                                :before-upload="beforeUpload">
                                <div style="font-size:15px;cursor:pointer"><p style="color:#1890ff;">上传图片</p></div>
                            </a-upload>
                            </br>
                            <a-icon style="margin-top:5px;" type="info-circle" />  为了保证显示效果，请上传750*422px大小的图片 ，支持jpg、jpeg、png文件大小不超过2M 
                        </a-form-model-item>
                    </a-col>
                    <a-col :span="24" :pull="4">
                        <a-form-model-item label=" " prop="liveCover">
                            <img v-if="form.liveCover==null" alt="封面图" style="width:35%;height:35%" src="../../../../assets/default-splash.png" />
                            <img v-if="form.liveCover!=null" alt="封面图" style="width:35%;height:35%" :src="form.liveCover" />
                        </a-form-model-item>
                    </a-col>
                    <a-col :span="24" :pull="4">
                        <a-form-model-item label="直播简介" prop="info" >
                        <a-input v-model="form.info" type="textarea" :maxLength="200" placeholder="请输入直播简介" style="width:50%"/>
                        </a-form-model-item>
                    </a-col>
                    <a-divider orientation="left">
                        <b>讲师信息</b>
                    </a-divider>
                    <a-col :span="24" :pull="4">
                        <a-form-model-item label="直播讲师" prop="lecturerName" >
                            <a-input placeholder="请选择讲师" v-model="form.lecturerName"  :disabled="true" style="width:50%">
                            <a-icon slot="addonAfter" type="plus-circle"   @click="toUserSelect()"/>
                            </a-input>
                            <br/>
                            <a-icon style="margin-top:5px;" type="info-circle" />  可以选择系统内存在的讲师，也可手动输入讲师信息 
                        </a-form-model-item>
                    </a-col>
                    <a-col :span="24" :pull="4">
                        <a-form-model-item label="讲师头像" prop="lecturerPhoto" >
                            <file-upload v-model="form.lecturerPhoto" type="image"></file-upload>
                        </a-form-model-item>
                    </a-col>
                    <a-col :span="24" :pull="4">
                        <a-form-model-item label="讲师简介" prop="lecturerInfo" >
                        <a-input v-model="form.lecturerInfo" type="textarea" :maxLength="200" placeholder="请输入讲师简介" style="width:50%"/>
                        </a-form-model-item>
                    </a-col>
                </a-row>
                <div class="bottom-control">
                    <a-space style="float:left;margin:10px 0px;">
                        <a-button type="primary" @click="submitForm" style="margin-left:60px;" shape="round">
                            保存并更新直播
                        </a-button>
                    </a-space>
                </div>
            </a-form-model>
        </a-card>
        <teacher-import ref="teacherInfoChap"  @ok="teacherConfirmChap"/>
    </div>
</template>
<script>
import allIcon from '@/core/icons'
import moment from 'moment';
import { getLive,updateLive } from '@/api/business/live'
import { uploadImg } from '@/api/common'
import teacherImport from "../../../common/teacher";
import { getUploadUrl } from '@/utils/request'
export default {
    name: 'LiveEdit',
    props: {
        live:{
            type: Object,
            default: null
        }
    },
    components: {
        teacherImport
    },
    data() {
        return {
             form: {
                liveId: null,
                pointId: null,
                userId: null,
                title: null,
                channelid: null,
                password: null,
                scene: null,
                template: null,
                delay: "N",
                online: null,
                type: null,
                liveStartTime: null,
                liveCover: null,
                status: '0',
                createBy: null,
                createTime: null,
                updateBy: null,
                updateTime: null,
                remark: null,
                orderNum: null,
                info: null,
                lecturerId: null,
                lecturerName: null,
                lecturerPhoto: null,
                lecturerInfo: null,
                liveWebLink: null,
                liveClientLink: null,
                liveViewLink: null,
                liveTutorLink: null,
                livePushPath: null,
                livePullPath: null
            },
            allIcon,
            info:{},
            labelCol: {
                xs: { span: 12 },
                sm: { span: 6 }
            },
            wrapperCol: {
                xs: { span: 24 },
                sm: { span: 18 }
            },
            liveTypeOptions:[],
            rules:{
                title: [
                  { required: true, message: '直播名称不能为空', trigger: 'blur' }
                ],
                liveStartTime: [
                  { required: true, message: '直播开始时间不能为空', trigger: 'change' }
                ],
                liveCover: [
                  { required: true, message: '直播封面图不能为空', trigger: 'blur' }
                ],
                info: [
                  { required: true, message: '直播简介不能为空', trigger: 'blur' }
                ],
                lecturerName: [
                  { required: true, message: '讲师姓名不能为空', trigger: 'change' }
                ],
                lecturerPhoto: [
                  { required: true, message: '讲师封面不能为空', trigger: 'blur' }
                ],
                lecturerInfo: [
                  { required: true, message: '讲师简介不能为空', trigger: 'blur' }
                ],
            },
            liveStartTime:null
        }
    },
    created () {
        this.handleUpdate(this.live.liveId);
        this.getDicts('app_live_type').then(response => {
            this.liveTypeOptions = response.data
        });
    },
    methods: {
       moment,
       handleUpdate (liveId) {
            this.reset();
            getLive(liveId).then(response => {
                this.form = response.data;
                this.liveStartTime=this.form.liveStartTime;
            });
       },
       beforeUpload(file) {
            const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
            if (!isJpgOrPng) {
                this.$message.error('请上传png或者jpg!');
            }
            const isLt5M = file.size / 1024 / 1024 < 10;
            if (!isLt5M) {
              this.$message.error('上传文件不能超过 10MB!')
               return false
            }
            let formData = new FormData();
            formData.append("file", file);
            uploadImg(formData).then(response => {
                    if (response.code === 200) {
                        this.form.liveCover=response.url;
                    } else {
                        this.msgError(response.msg);
                    }
            });
       },
       range(start, end) {
            const result = [];
            for (let i = start; i < end; i++) {
                result.push(i);
            }
            return result;
       },
       disabledDate(current) {
          return current && current < moment().subtract(1, 'days').endOf('day');
       },
       reset () {
            this.form = {
                liveId: null,
                pointId: null,
                userId: null,
                title: null,
                channelid: null,
                password: null,
                scene: null,
                template: null,
                delay: "N",
                online: null,
                type: null,
                liveStartTime: null,
                liveCover: null,
                status: '0',
                createBy: null,
                createTime: null,
                updateBy: null,
                updateTime: null,
                remark: null,
                orderNum: null,
                info: null,
                lecturerId: null,
                lecturerName: null,
                lecturerPhoto: null,
                lecturerInfo: null,
                liveWebLink: null,
                liveClientLink: null,
                liveViewLink: null,
                liveTutorLink: null,
                livePushPath: null,
                livePullPath: null
            }
       },
       toUserSelect(){
            this.$refs.teacherInfoChap.show(this.form.pointId,'nb');
        },
        teacherConfirmChap(teacher){
            this.form.lecturerId=teacher[0].teacherId;
            this.form.lecturerName=teacher[0].nickName;
            this.form.lecturerPhoto=teacher[0].teacherImg;
            this.form.lecturerInfo=teacher[0].info;
        },
       /** 提交按钮 */
       submitForm: function () {
        this.$refs.form.validate(valid => {
            if (valid) {
                var date1=new Date(this.liveStartTime);
                var date2=new Date();
                var m=parseInt(Math.abs(date2-date1)/1000/60);
                if(m<25){
                    this.$message.info('距直播开始时间已小于25分钟!',3);
                    this.form.liveStartTime=this.liveStartTime;
                    return false;
                }
                if(this.form.status=="ybh"){
                    this.form.status="shz";
                }
                if (this.form.liveId !== undefined && this.form.liveId !== null) {
                        updateLive(this.form).then(response => {
                        this.$message.success(
                            '修改成功',
                            3
                        )
                        this.$emit('ok')
                    })
                }
            }else{
                return false;
            }
         });
       },
    }
}
</script>
<style lang="less">
.ant-upload-list{
    display: none;
}
.live-add{
    .ant-form{
        .ant-col{
            label{
                font-size: 15px;
            }
        }
    }
    .live-yc{
        //border:1px solid #ddd;
        margin:0 auth;
        text-align:center;
        line-height: 60px!important;
        white-space: nowrap;
    }
}
</style>