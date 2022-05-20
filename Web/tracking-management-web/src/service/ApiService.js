import { API_BASE_URL } from "../app-config";
const ACCESS_TOKEN = "ACCESS_TOKEN";

export async function call(api, method, request) {
  let headers = new Headers({
    "Content-Type": "application/json",
  });

  // 로컬 스토리지에서 ACCESS TOKEN 가져오기
  const accessToken = localStorage.getItem("ACCESS_TOKEN");
  if (accessToken && accessToken !== null) {
    headers.append("Authorization", "Bearer " + accessToken);
  }

  let options = {
    headers: headers,
    url: API_BASE_URL + api,
    method: method,
  };

  if (request) {
    // GET method
    options.body = JSON.stringify(request);
  }
  return fetch(options.url, options)
    .then((response) =>
      response.json().then((json) => {
        if (!response.ok) {
          // response.ok가 true이면 정상적인 리스폰스를 받은것, 아니면 에러 리스폰스를 받은것.
          return Promise.reject(json);
        }
        return json;
      })
    )
    .catch((error) => {
      // 추가된 부분
      console.log(error.status);
      if (error.status === 403) {
        window.location.href = "/error"; // redirect
      }
      return Promise.reject(error);
    });
}

export async function signin(webDTO) {
  const response = await call("/system/signin", "POST", webDTO).catch((error) => {alert("입력 정보가 올바르지 않습니다.");});
    if (response.token) {
        // 로컬 스토리지에 토큰 저장
        localStorage.setItem(ACCESS_TOKEN, response.token);
        // token이 존재하는 경우 유저 메인 화면으로 리디렉트
        window.location.href = "/main";
    }

}

export function signout() {
  localStorage.setItem(ACCESS_TOKEN, null);
  window.location.href = "/login";
}

export function signup(webDTO) {
  return call("/system/signup", "POST", webDTO);
}
