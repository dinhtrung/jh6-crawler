#!/bin/bash

rsync -avP target/*.jar root@app2.daiduongdsa.vn:/srv/crawler/
ssh root@app2.daiduongdsa.vn systemctl restart crawler
