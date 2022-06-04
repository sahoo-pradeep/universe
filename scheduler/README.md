Local Setup
1. Install [Docker Hub](https://docs.docker.com/desktop/windows/install/) 
2. Run postgres: `docker run --name postgres -e POSTGRES_PASSWORD=postgres -d -p 5432:5432 postgres:14-alpine`
3. Open postgres container in terminal: `docker exec -it postgres bash`
4. Connect psql: `psql postgres postgres`
5. Run below commands to create a database, user and grant permission
```
  create database scheduler;
  create user scheduler with encrypted password 'scheduler';
  grant all privileges on database scheduler to scheduler;
```