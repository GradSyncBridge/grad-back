<template>
  <el-row>
    <el-col :span="12">
        <div class="comment-section">
            <div class="comment-submit">
                <h2 class="el-h2">Comment</h2>
                <form @submit.prevent="submitComment">
                <textarea v-model="newComment" placeholder="Please enter your comment"></textarea>
                <el-button type="primary" native-type="submit">Leave a comment</el-button>
                </form>
            </div>
        </div>
    </el-col>
    <el-col :span="12">
        <div class="comment-history">
            <h2 class="el-h2">History Comment</h2>
            <div class="history-comment-container">
            <ul>
                <li v-for="(comment, index) in filterComments" :key="index">
                {{ comment.content }} - {{ timeAgo(comment.timestamp) }}
                </li>
            </ul>
            </div>
        </div>
    </el-col>
  </el-row>
</template>
  
  <script>
  export default {
    name: "CommentSection",
    data() {
      return {
        newComment: "",
        comments: []
      };
    },
    computed: {
       filterComments() {
        this.sortedComments();
        return this.comments.filter((comment, index) => index <=7);
      },
    },
    methods: {
        sortedComments() {
            return this.comments.sort((a, b) => {
              return new Date(b.timestamp) - new Date(a.timestamp);
            });
       },
      submitComment() {
        // 将新评论提交到后端数据库
        this.comments.push({
          content: this.newComment,
          timestamp: new Date()
        });
        // 提交后清空输入框
        this.newComment = "";
      },
      fetchComments() {
        // 获取历史评论的方法
      },
      timeAgo(timestamp) {
        const seconds = Math.floor((new Date() - new Date(timestamp)) / 1000);
        let interval = Math.floor(seconds / 31536000);
  
        if (interval > 1) {
          return interval + " years ago";
        }
        interval = Math.floor(seconds / 2592000);
        if (interval > 1) {
          return interval + " months ago";
        }
        interval = Math.floor(seconds / 86400);
        if (interval > 1) {
          return interval + " days ago";
        }
        interval = Math.floor(seconds / 3600);
        if (interval > 1) {
          return interval + " hours ago";
        }
        interval = Math.floor(seconds / 60);
        if (interval > 1) {
          return interval + " minutes ago";
        }
        return Math.floor(seconds) + " seconds ago";
      }
    },
    mounted() {
      this.fetchComments();
    }
  };
  </script>
  
  <style>
  .comment-section {
    display: flex;
    justify-content: space-around;
    max-width: 80%;
    max-height: 100%;
    margin: 0 auto;
  }
  
  .comment-submit {
    width: 45%;
  }
  
  .comment-history {
    width: 45%;
  }
  
  .el-h2 {
    width: 100%;
    height: 25%;
    padding: 2% 2% 0 2%;
    color: white;
    margin: 0;
    overflow: hidden;
    font-size: 30px;
  }
  
  textarea {
    width: 99%;
    height: 100%;
    margin-bottom: 5%;
    padding: 5%;
    box-sizing: border-box;
    font-size: 20px;
    resize: none;
  }
  
  button {
    padding: 120%;
    background-color: #47a3da;
    color: #fff;
    border: none;
    cursor: pointer;
  }
  
  button:hover {
    background-color: #5f7077;
  }
  
  .history-comment-container {
    max-height: 50%;
    overflow: auto;
    border: 1px solid #727272f1;
    padding: 2%;
    color: #ffffff;
    resize: none;
  }
  </style>