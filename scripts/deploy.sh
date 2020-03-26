#!/bin/bash

./mvnw -Pprod clean package -DskipTests=true

rsync -avP target/*.jar root@app2.daiduongdsa.vn:/srv/crawler/
ssh root@app2.daiduongdsa.vn systemctl restart crawler

