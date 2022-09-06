let backendHost;

const hostname = window && window.location && window.location.hostname;

backendHost = "http://TMS-SERVER.ap-northeast-2.elasticbeanstalk.com"


export const API_BASE_URL = `${backendHost}`;
