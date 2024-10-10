## 设置 kibana_system密码
curl -X POST "http://nexfly-elasticsearch:9200/_security/user/kibana_system/_password" -H "Content-Type: application/json" -u elastic:nexfly9527 -d '
{
"password" : "nexfly9527"
}'