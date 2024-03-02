<template>
    <el-row class="el-row">
        <el-col :span="24">
            <div class="grid-content ep-bg-purple-dark title">
                <h1>Travel Website</h1>
                <h2 >Give you wonderful exprience and advanced news</h2>
                <el-button type="primary" size="large" dark="true" round>Start Journey<el-icon style="margin-left: 2%;"><Right /></el-icon></el-button>
            </div>
        </el-col>
    </el-row>
</template>
<script>
export default{
    name: 'FirstPage1',
    data(){
    },
    methods: {
    },
}
</script>

<style scoped>
.el-row{
    width: 100%;
    height: 100%;
}
.title{
    min-width: 100%;
    min-height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    margin: 0 auto;
    color: white;
    text-align: center;
}
.title h1{
   font-size: 3.5em;
}
.title h2{
    font-size: 1.5em;
}
.el-button{
    margin-top: 20px;
    font-size: large;
    text-align: center;
}
</style>