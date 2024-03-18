
// 이미지 첨부 후 첨부 파일 나오게 하기
function previewImage(event) {
    const input = event.target;
    const reader = new FileReader();

    reader.onload = function () {
        const imageUploadBox = document.querySelector(".image-upload-box");

        // 이미지 업로드 상자 내에 이미지 요소 추가
        const previewImage = document.createElement("img");
        previewImage.src = reader.result;
        previewImage.alt = "미리보기 이미지";

        // 이미지 스타일 설정
        previewImage.style.width = "100%"; // 이미지를 부모 요소에 맞게 100%로 설정
        previewImage.style.height = "auto"; // 이미지의 높이를 가로 비율에 맞게 자동으로 조절
        previewImage.style.display = "block"; // 이미지 표시

        // 이미지 업로드 상자에 이미지 추가
        imageUploadBox.innerHTML = ""; // 기존 내용 삭제
        imageUploadBox.appendChild(previewImage);

        // 이미지의 실제 크기를 가져옴
        const img = new Image();
        img.src = reader.result;
        img.onload = function () {
            // 이미지의 크기를 CSS를 사용하여 조절
            previewImage.style.width = "430px"; // 이미지를 부모 요소에 맞게 100%로 설정
            previewImage.style.height = "170px"; // 이미지의 높이를 가로 비율에 맞게 자동으로 조절
        };
    };

    reader.readAsDataURL(input.files[0]);
}

//크루 이름
document.getElementById("crewName").addEventListener("change", function () {
  var crewName = document.getElementById("crewName").value;
  console.log("크루 이름 :", crewName);
});

//위치 선택 팝업
var openModalBtns = document.getElementsByClassName("openAreaModal");
for (var i = 0; i < openModalBtns.length; i++) {
  openModalBtns[i].addEventListener("click", function () {
    document.getElementById("selectArea").style.display = "block";
  });
}

// 위치 팝업 닫기
document
  .getElementsByClassName("closeArea")[0]
  .addEventListener("click", function () {
    document.getElementById("selectArea").style.display = "none";
  });

// 위치 검색 기능 구현
document
  .getElementById("searchAreaInput")
  .addEventListener("keyup", function () {
    var input, filter, ul, li, i, txtValue;
    input = document.getElementById("searchAreaInput");
    filter = input.value.toUpperCase();
    ul = document.getElementById("area");
    li = ul.getElementsByTagName("li");
    for (i = 0; i < li.length; i++) {
      txtValue = li[i].textContent || li[i].innerText;
      if (txtValue.toUpperCase().indexOf(filter) > -1) {
        li[i].style.display = "";
      } else {
        li[i].style.display = "none";
      }
    }
  });

// 위치 선택
document.getElementById("area").addEventListener("click", function (e) {
  var selectedArea = e.target.textContent;
  console.log("위치:", selectedArea);

  // 선택 부분 업데이트
  var selectedElement = document.querySelector(".right_area button");
  selectedElement.textContent = selectedArea;

  document.getElementById("selectArea").style.display = "none"; // 팝업 닫기
});

// 정원 선택 팝업
var openMaxPeopleBtns = document.getElementsByClassName("openMaxPeopleModal");
for (var i = 0; i < openMaxPeopleBtns.length; i++) {
  openMaxPeopleBtns[i].addEventListener("click", function () {
    document.getElementById("selectMaxPeople").style.display = "block";
  });
}

// 정원 팝업 닫기
document.getElementById("confirm").addEventListener("click", function () {
  document.getElementById("selectMaxPeople").style.display = "none";
});
document.querySelector(".closePeople").addEventListener("click", function () {
  document.getElementById("selectMaxPeople").style.display = "none";
});
// 정원 선택 이벤트
document
  .getElementById("searchPeopleInput")
  .addEventListener("change", function () {
    var maxPeopleInput = document.getElementById("searchPeopleInput");
    var maxPeople = parseInt(maxPeopleInput.value); // 입력된 값 숫자로 변환

    if (maxPeople > 100) {
      alert("정원은 100명 이하로 설정해주세요."); // 20 이상인 경우 경고 표시
      maxPeopleInput.value = 100; // 입력된 값이 20 초과이면 최댓값인 20으로 변경
      maxPeople = 100; // maxPeople 변수도 최댓값인 20으로 설정
    }
    console.log("정원:", maxPeople);

    // 회원 수 업데이트
    var maxPeopleElement = document.querySelector(".right_maxPeople button");
    maxPeopleElement.textContent = maxPeople;

    document.getElementById("selectMaxPeople").style.display = "none"; // 팝업 닫기
  });

//모임 소개
document.querySelector(".closePeople").addEventListener("click", function () {
  document.getElementById("selectMaxPeople").style.display = "none";
});

