/**
 * 
 */

document.addEventListener("DOMContentLoaded", () => {

    const slides = document.querySelectorAll(".slide");
    let currentIndex = 0;

    setInterval(() => {
        // 今の画像を消す
        slides[currentIndex].classList.remove("active");

        // 次の画像へ
        currentIndex = (currentIndex + 1) % slides.length;

        // 次の画像を表示
        slides[currentIndex].classList.add("active");

    }, 3000); // 3秒ごと
});
