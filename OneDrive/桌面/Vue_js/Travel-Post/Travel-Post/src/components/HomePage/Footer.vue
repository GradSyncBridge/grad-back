<template>
    <footer>
       
       <div class="container-footer-all">
        
            <div class="container-body">

                <div class="colum1">
                    <h1>More Information About The Group</h1>

                    <p style="font-size: large;">Our group is dedicated to connecting people
                        who want to travel to different places in the world, 
                        we offer you the best experience in your life,
                        we have the best travel news,
                        we have the best recommendations for you.</p>

                </div>

                <div class="colum2">
                    <h1>Social Networks</h1>
                    <div class="row1">
                        <img src="/src/icon/facebook.png">
                        <label>Contact us on Facebook</label>
                    </div>
                    <div class="row1">
                        <img src="/src/icon/twitter.png">
                        <label>Contact us on Twitter</label>
                    </div>
                    <div class="row1">
                        <img src="/src/icon/instagram.png">
                        <label>Contact us on Instagram</label>
                    </div>
                    <div class="row1">
                        <img src="/src/icon/google-plus.png">
                        <label>Contact us on Google Plus</label>
                    </div>
                    <div class="row1">
                        <img src="/src/icon/pinterest.png">
                        <label>Contact us on Pinteres</label>
                    </div>


                </div>

                <div class="colum3">

                    <h1>More Information</h1>

                    <div class="row2">
                        <el-icon style="font-size: 36px;"><Avatar /></el-icon>
                        <label>
                            MarHongBin，
                            YanRan，
                            HuangBoSheng，
                            LaiShanQuan，
                            HeQingLin 
                        </label>
                    </div>

                    <div class="row2">
                        <img src="/src/icon/smartphone.png">
                        <label>+86 13825769160</label>
                    </div>

                    <div class="row2">
                        <img src="/src/icon/contact.png">
                         <label>qinglinhe44@gmail.com</label>
                    </div>

                </div>

            </div>
        
        </div>
        
        <div class="container-footer">
               <div class="footer">
                    <div class="copyright">
                        © 2024 All Rights Reserved | <a href="">Inspration</a>
                    </div>

                    <div class="information">
                        <a href="">Group Information</a> | <a href="">Privatization and Politics</a> | <a href="">Terms and Conditions</a>
                    </div>
                </div>
            </div>
        
    </footer>

</template>
<script>
export default{
    name: 'Footer',
    data(){
    },
    methods: {
    },
}
</script>
<style scoped>

footer{
    width: 100% !important;
    background: #202020 !important; 
    color: white !important;   
    margin: 0 auto !important;
    padding: 0 !important;
}

.container-footer-all{
    width: 100% !important;
    max-width: 1200px !important;
    margin: auto !important;
    padding: 40px !important;
}

.container-body{
    display: flex !important;
    justify-content: space-between !important;
}

.colum1{
    max-width: 400px !important;
}

.colum1 h1{
    font-size: 22px !important;
}

.colum1 p{
    font-size: 14px !important;
    color: #C7C7C7 !important;
    margin-top: 20px !important;
}

.colum2{
    max-width: 400px !important;
    
}

.colum2 h1{
    font-size: 22px !important;
}

.row1{
    margin-top: 20px !important;
    display: flex !important;
}

.row1 img{
    width: 36px !important;
    height: 36px !important;
}

.row1 label{
    margin-top: 10px !important;
    margin-left: 20px !important;
    color: #C7C7C7 !important;
}

.colum3{
    max-width: 400px !important;
}

.colum3 h1{
    font-size: 22px !important;
}

.row2{
    margin-top: 20px !important;
    display: flex !important;
}

.row2 img{
    width: 36px !important;
    height: 36px !important;
}

.row2 label{
    margin-top: 10px !important;
    margin-left: 20px !important;
    max-width: 140px !important;
}

.container-footer{
    width: 100% !important;  
    background: #101010 !important;
}

.footer{
    max-width: 1200px !important;
    margin: auto !important;
    display: flex !important;
    justify-content: space-between !important;  
    padding: 20px !important;
}

.copyright{
    color: #C7C7C7 !important;
}

.copyright a{
    text-decoration: none !important;
    color: white !important;
    font-weight: bold !important; 
}

.information a{
    text-decoration: none !important;
    color: #C7C7C7 !important;
}

</style>