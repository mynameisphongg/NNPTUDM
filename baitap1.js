//HỌ VÀ TÊN: NGUYỄN NGỌC PHONG
//MSSV: 2180605077




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

// 1. Sử dụng map để in ra xếp loại của từng sinh viên
students.map(student => {
    console.log(`${student.name} - Xếp loại: ${student.getClassification()}`);
});

// 2. Sử dụng reduce để tính TBC điểm của lớp
const averageClassScore = students.reduce((acc, student) => acc + student.getAverage(), 0) / students.length;
console.log(`Điểm trung bình của lớp: ${averageClassScore.toFixed(2)}`);

// 3. Sử dụng some để kiểm tra xem có sinh viên nào dưới 18 hay không
const hasUnder18 = students.some(student => student.age < 18);
console.log(`Có sinh viên dưới 18 tuổi không? ${hasUnder18 ? "Có" : "Không"}`);

// 4. Hiển thị danh sách sinh viên dưới 18 tuổi
const under18Students = students.filter(student => student.age < 18);
if (under18Students.length > 0) {
    console.log("Sinh viên dưới 18 tuổi:");
    under18Students.forEach(student => console.log(`- ${student.name}`));
} else {
    console.log("Không có sinh viên nào dưới 18 tuổi.");
}

// 5. Sử dụng every để kiểm tra cả lớp có đầy đủ tên hay không
const allHaveNames = students.every(student => student.name.trim().length > 0);
console.log(`Tất cả sinh viên có đầy đủ tên không? ${allHaveNames ? "Có" : "Không"}`);
