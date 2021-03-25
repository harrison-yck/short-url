<p align="center">
    <a href="" width="200" rel="noopener">
    <img src="https://image.flaticon.com/icons/svg/825/825250.svg"  width="200" alt="Short Url"></a>
</p>

<h1 align="center">Short Url</h1>

<p align="center">A small project for shorten url application</p>

## Table of Contents

- [Features](#features)
- [Design](#design)
- [Limitation](#limitation)
- [Authors](#authors)

## Features



## How To Use
1. Create a kubernetes (K8s) cluster * (it is possible to use other orchestration engine, but you might need to prepare resource files by yourself).
2. Start the cluster (command: minikube start)
3. Mount data and datalog directory for zookeeper (command: minikube mount PATH_TO/data:/data, minikube mount PATH_TO/datalog:/datalog)
4. Apply yaml files in kube directory to create kubernetes resources (command: kubectl apply --recursive -f PATH_TO/kube)
5. Port forward website to localhost (command: kubectl port-forward deploy/website 8443:8443)
6. Open browser and go to https://localhost:8443 (your browser may warn you that the connection is not secure as there is no certificate, just ignore it)
7. Enter your long url into the textbox
8. Copy and paste shorten url in search box. Congrats!

<p style="font-size: 12.5px">* You can use software like <u>minikube</u> / <u>MicroK8s</u> to create a K8s cluster for testing purpose, I recommend minikube since it is easier to setup</p>

## Design

## Limitation

## Authors

- [@harrison](https://github.com/harrison-yck)
