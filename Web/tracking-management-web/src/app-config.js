let backendHost;

const hostname = window && window.location && window.location.hostname;

if (hostname === "localhost") {
  backendHost = "http://PROD-TMS-BACKEND.ap-northeast-2.elasticbeanstalk.com";
  // backendHost = "http://localhost:8080"
}

export const API_BASE_URL = `${backendHost}`;
