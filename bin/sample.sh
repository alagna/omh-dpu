curl http://localhost:8080/omh/dpu/bloodPressure ; echo ""

curl http://localhost:8080/omh/dpu/bloodPressure -X POST -H "Content-Type: application/json" -d '{"systolic":100, "diastolic":80}' ; echo ""

# trying to throw BusinessError
curl http://localhost:8080/omh/dpu/bloodPressure -X POST -H "Content-Type: application/json" -d '{"systolic":10, "diastolic":82}' ; echo ""