let backendHost;

const hostname = window && window.location && window.location.hostname;

if (hostname === "localhost") {
 
  //backendHost = "http://localhost:8080"
  // backendHost = "http://211.245.10.65:8080"
  backendHost = "http://TMS-SERVER.us-west-2.elasticbeanstalk.com"
}
else {
  //backendHost = "http://211.245.10.65:8080"
  backendHost = "http://TMS-SERVER.us-west-2.elasticbeanstalk.com"
}

export const API_BASE_URL = `${backendHost}`;
