class Student {
    constructor(name, age, score1, score2) {
        this.name = name;
        this.age = age;
        this.score1 = score1;
        this.score2 = score2;
    }

    getAverage() {
        return (this.score1 + this.score2) / 2;
    }

    getClassification() {
        const avg = this.getAverage();
        if (avg >= 9) return "Xuất sắc";
        if (avg >= 8) return "Giỏi";
        if (avg >= 6.5) return "Khá";
        if (avg >= 5) return "Trung bình";
        if (avg >= 3.5) return "Yếu";
        return "Kém";
    }
}

const students = [
    new Student("Nguyễn Ngọc Phong", 19, 8.5, 9),
    new Student("Lê Như Tâm", 17, 7, 6.5),
    new Student("Nguyễn Khang Hy", 20, 5, 6),
    new Student("Nguyễn Thiện Nhân", 18, 9, 9.5)
];

function getRandomNumber() {
    return Math.floor(Math.random() * 11);
}

function createPromise(delay, promiseName) {
    return new Promise((resolve, reject) => {
        console.log(`${promiseName} đang chạy, chờ ${delay / 1000} giây...`);
        setTimeout(() => {
            const randomNumber = getRandomNumber(); // Lấy số ngẫu nhiên mới cho mỗi promise
            console.log(`${promiseName} tạo số ngẫu nhiên: ${randomNumber}`);
            
            if (randomNumber % 2 === 0) {
                console.log(`${promiseName} hoàn thành, trả về sinh viên:`);
                console.log(students[0]);
                resolve(students[0]); // Trả về một đối tượng student
            } else {
                console.log(`${promiseName} thất bại: Dữ liệu lỗi`);
                reject(new Error(`${promiseName} thất bại: Dữ liệu lỗi`));
            }
        }, delay);
    });
}

const promise1 = createPromise(2000, "Promise 1");
const promise2 = createPromise(4000, "Promise 2");

// Sử dụng Promise.all với xử lý lỗi
Promise.all([promise1, promise2])
    .then(results => {
        console.log("\n Cả hai promise đã hoàn thành! Dữ liệu lấy được:");
        console.log(results);
        console.log("Lấy dữ liệu hoàn thành!\n");
    })
    .catch(error => {
        console.error("\n Lỗi trong Promise.all:", error.message);
    });

// Sử dụng Promise.race với xử lý lỗi
Promise.race([promise1, promise2])
    .then(result => {
        console.log("\n Promise nhanh nhất hoàn thành! Dữ liệu lấy được:");
        console.log(result);
        console.log("Đã lấy được dữ liệu!\n");
    })
    .catch(error => {
        console.error("\n Lỗi trong Promise.race:", error.message);
    });
