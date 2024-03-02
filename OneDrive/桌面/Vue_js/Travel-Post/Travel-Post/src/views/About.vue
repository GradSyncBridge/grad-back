<!-- <template>
  <div>
      <el-button type="primary" @click="loginWithGitHub()">Primary</el-button>
  </div>
</template>
  <script>
  export default {
    data() {
      return {
        clientId: '',
        clientSecret: '',
        code: '',
        redirectUri: '',
        authUrl: '',
        urlParams: '',
      };
    },
    methods: {
      async mounted() {
  // 检查 URL 是否包含授权码
  const urlParams = new URLSearchParams(window.location.search);
  const code = urlParams.get('code');

  if (code) {
    // 授权码存在，继续获取访问令牌
    try {
      const accessToken = await this.getAccessToken(this.clientId, this.clientSecret, code, this.redirectUri);
      console.log('Access Token:', accessToken);

      // 获取访问令牌后，你可能还需要执行其他操作，例如获取用户信息等
      // ...

      // 最后，重定向到应用内的其他页面，以确保 URL 不再包含敏感信息
      this.$router.push('/about');
    } catch (error) {
      console.error('获取访问令牌失败:', error);
    }
  }
},

      async loginWithGitHub() {
        try {
          this.clientId = '4e9aa0417084842c9a57';
          this.redirectUri = 'http://localhost:5173/about';
          this.clientSecret = 'a581982cb6f8e8df26ab117b4d6cafee323f1910';
          // 构建授权 URL
          this.authUrl = `https://github.com/login/oauth/authorize?client_id=${this.clientId}&redirect_uri=${this.redirectUri}&scope=user`;
  
          // 打开新窗口或重定向到 GitHub 授权页面
          const currentUrlParams = new URLSearchParams(window.location.search);
          const codeFromUrl = currentUrlParams.get('code');

          // 如果当前 URL 中有 code 参数，则使用它，否则进行重定向
            this.code = codeFromUrl;
            await this.getAccessToken();
            // 打开新窗口或重定向到 GitHub 授权页面
            console.log('Completed');
            window.location.href = this.authUrl;
          console.log('Access Token:', accessToken);
          console.log('Completed');

        } catch (error) {
          console.error('GitHub Authentication Error:', error);
        }
      },
      async getAccessToken(clientId, clientSecret, code, redirectUri) {
          const url = 'https://github.com/login/oauth/access_token';
          const data = {
            client_id: clientId,
            client_secret: clientSecret,
            code: code,
            redirect_uri: redirectUri,
          };

          const response = await fetch(url, {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
              'Accept': 'application/json',
            },
            body: JSON.stringify(data),
          });

          const result = await response.json();
  
          if (result.access_token) {
            console.log('Completed');
            return result.access_token;
          } else {
            throw new Error('Failed to get access token');
          }
       }
    },
  };
  </script> -->

<!-- <style scoped>
  .ButtonClass {
    width: 200px !important;
    height: 40px !important;
    border-radius: 4px;
    background-color:white;
    color: #fff;
    font-size: 16px;
    cursor: pointer;
  }
</style> -->
<!-- 
curl -X POST -H "Content-Type: application/json" -d '{"status": "success", "message": "File uploaded successfully", "imagePath": "/uploads/image.jpg"}' https://run.mocky.io/v3/62b08f7c-7b82-45fe-a027-17fce2fe32cb -->


<template>
  <el-upload
    class="avatar-uploader"
    action="https://eodk2bhoiulqtnx.m.pipedream.net"
    :show-file-list="false"
    :on-success="handleAvatarSuccess"
    :before-upload="beforeAvatarUpload"
  >
    <img v-if="imageUrl" :src="imageUrl" class="avatar" />
    <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
    <!-- //输出图片路径 -->
    <div v-if="imageUrl != null">
     <p>{{imageUrl}}</p>
    </div>
  </el-upload>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

import type { UploadProps } from 'element-plus'

const imageUrl = ref('')

const handleAvatarSuccess: UploadProps['onSuccess'] = (response, uploadFile) => {
  // 使用 FileReader 读取文件并转换为 Data URL
  console.log('Upload success. Response:', response);
  console.log('Uploaded file:', uploadFile);
  const reader = new FileReader();
  reader.onload = (event) => {
    imageUrl.value = event.target?.result as string;
  };
  reader.readAsDataURL(uploadFile.raw!);
}

const beforeAvatarUpload: UploadProps['beforeUpload'] = (rawFile) => {
  if (rawFile.type !== 'image/jpeg') {
    ElMessage.error('Avatar picture must be JPG format!')
    return false
  } else if (rawFile.size / 1024 / 1024 > 2) {
    ElMessage.error('Avatar picture size can not exceed 2MB!')
    return false
  }
  return true
}
</script>

<style scoped>
.avatar-uploader .avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}
</style>
