package com.brandjunhoe.springbatch.tasklet

class CustomService {

    fun businessLogic() {

        // 비즈니스 로직
        for (index in 0..9) {
            println("[index] = $index")
        }

    }

}