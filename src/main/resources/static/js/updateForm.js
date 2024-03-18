

nicknameCheck = () => {
  const nickname = document.getElementById("memberNickname").value;
  const checkResult = document.getElementById("checkNicknameResult");
  $.ajax({
    type: "post",
    url: "/member/nicknameCheck",
    data: {
      memberNickname: nickname,
    },
    success: function (res) {
      if (res == "ok") {
        checkResult.style.color = "green";
        checkResult.innerHTML = "사용가능한 닉네임 입니다.";
      } else {
        checkResult.style.color = "red";
        checkResult.innerHTML = "이미 사용중인 닉네임 입니다.";
      }
    },
    error: function (err) {
      console.log("에러발생 :", err);
    },
  });
};

//비밀번호 일치 확인 유효성 검사

document.getElementById("password").addEventListener("input", function() {
  var password = document.getElementById("password").value;
  var dbPassword = "여기에 데이터베이스에서 가져온 비밀번호를 설정하세요"; // 데이터베이스 비밀번호
  var newPasswordInput = document.querySelector(".passwordCheck");
  var passwordCheckInput = document.getElementById("passwordCheck");
  var passwordError = document.getElementById("passwordError");

  if (password === dbPassword) {
    newPasswordInput.style.display = "block"; // 새 비밀번호 입력란 보이기
    passwordError.textContent = "";
  } else {
    newPasswordInput.style.display = "none"; // 새 비밀번호 입력란 숨기기
    passwordCheckInput.value = ""; // 새 비밀번호 입력란 초기화
    passwordError.textContent = "현재 비밀번호가 일치하지 않습니다.";
  }
});
document.getElementById("passwordCheck").addEventListener("input", function() {
  var password = document.getElementById("password").value;
  var passwordCheck = document.getElementById("passwordCheck").value;
  var passwordError = document.getElementById("passwordError");

  if (password === passwordCheck) {
    passwordError.textContent = "비밀번호가 일치합니다.";
    passwordError.classList.add("success");
    passwordError.classList.remove("error");
  } else {
    passwordError.textContent = "비밀번호가 일치하지 않습니다.";
    passwordError.classList.add("error");
    passwordError.classList.remove("success");
  }
});

function sample6_execDaumPostcode() {
  new daum.Postcode({
    oncomplete: function (data) {
      // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

      // 각 주소의 노출 규칙에 따라 주소를 조합한다.
      // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
      var addr = ""; // 주소 변수
      var extraAddr = ""; // 참고항목 변수

      //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
      if (data.userSelectedType === "R") {
        // 사용자가 도로명 주소를 선택했을 경우
        addr = data.roadAddress;
      } else {
        // 사용자가 지번 주소를 선택했을 경우(J)
        addr = data.jibunAddress;
      }

      // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
      if (data.userSelectedType === "R") {
        // 법정동명이 있을 경우 추가한다. (법정리는 제외)
        // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
        if (data.bname !== "" && /[동|로|가]$/g.test(data.bname)) {
          extraAddr += data.bname;
        }
        // 건물명이 있고, 공동주택일 경우 추가한다.
        if (data.buildingName !== "" && data.apartment === "Y") {
          extraAddr +=
            extraAddr !== "" ? ", " + data.buildingName : data.buildingName;
        }
        // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
        if (extraAddr !== "") {
          extraAddr = " (" + extraAddr + ")";
        }
        // 조합된 참고항목을 해당 필드에 넣는다.
        document.getElementById("sample6_extraAddress").value = extraAddr;
      } else {
        document.getElementById("sample6_extraAddress").value = "";
      }

      // 우편번호와 주소 정보를 해당 필드에 넣는다.
      document.getElementById("sample6_postcode").value = data.zonecode;
      document.getElementById("sample6_address").value = addr;
      // 커서를 상세주소 필드로 이동한다.
      document.getElementById("sample6_detailAddress").focus();
    },
  }).open();
}

// 파일 선택 시 이미지 미리보기 함수
function previewImage(event) {
  if (event.target.files.length > 0) {
    // 파일 객체 가져오기
    const file = event.target.files[0];

    // FileReader 객체 생성
    const reader = new FileReader();

    // FileReader 로드 완료 시 동작할 함수 정의
    reader.onload = function (event) {
      // 이미지 미리보기를 위한 img 요소 가져오기
      const imagePreview = document.getElementById("imagePreview");

      // FileReader에서 읽은 데이터를 img 요소에 설정하여 미리보기
      imagePreview.src = event.target.result;
    };

    // 파일을 Data URL 형태로 읽기
    reader.readAsDataURL(file);
  }
}
