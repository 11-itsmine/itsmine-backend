import React, {useEffect, useState} from 'react';
import axiosInstance from '../../api/axiosInstance';
import styled from 'styled-components';

const ReportedUsers = () => {
  const [reportedUsers, setReportedUsers] = useState([]);
  const [error, setError] = useState('');
  const [selectedReport, setSelectedReport] = useState(null);

  useEffect(() => {
    axiosInstance.get('/v1/report')
    .then(response => {
      console.log(response.data);
      // data.content 배열을 사용
      if (response.data && Array.isArray(response.data.data.content)) {
        setReportedUsers(response.data.data.content);
      } else {
        setError('신고된 유저 데이터를 불러올 수 없습니다.');
        setReportedUsers([]);
      }
    })
    .catch(() => {
      setError('신고된 유저 데이터를 불러올 수 없습니다.');
      setReportedUsers([]);
    });
  }, []);

  const handleReportClick = (report) => {
    setSelectedReport(report);
  };

  const handleBackToList = () => {
    setSelectedReport(null);
  };

  const handleCompleteStatus = () => {
    if (selectedReport) {
      axiosInstance.put(`/v1/report/complete/${selectedReport.reportId}`)
      .then(() => {
        alert('신고 상태가 완료 처리되었습니다.');
        window.location.reload(); // 페이지 리프레쉬
      })
      .catch(() => {
        alert('신고 상태를 완료 처리하는 중 오류가 발생했습니다.');
      });
    }
  };

  return (
      <Container>
        {error && !reportedUsers.length && <ErrorText>{error}</ErrorText>}
        {selectedReport ? (
            <DetailContainer>
              <DetailTitle>신고 상세 정보</DetailTitle>
              <DetailTable>
                <tbody>
                <DetailRow>
                  <DetailTh>신고 ID:</DetailTh>
                  <DetailTd>{selectedReport.reportId}</DetailTd>
                </DetailRow>
                <DetailRow>
                  <DetailTh>신고자:</DetailTh>
                  <DetailTd>{selectedReport.username}</DetailTd>
                </DetailRow>
                <DetailRow>
                  <DetailTh>신고자 닉네임:</DetailTh>
                  <DetailTd>{selectedReport.nickName}</DetailTd>
                </DetailRow>
                <DetailRow>
                  <DetailTh>신고받은자:</DetailTh>
                  <DetailTd>{selectedReport.badPerson}</DetailTd>
                </DetailRow>
                <DetailRow>
                  <DetailTh>신고받은자 닉네임:</DetailTh>
                  <DetailTd>{selectedReport.badPersonNickname}</DetailTd>
                </DetailRow>
                <DetailRow>
                  <DetailTh>제목:</DetailTh>
                  <DetailTd>{selectedReport.title || '정보 없음'}</DetailTd>
                </DetailRow>
                <DetailRow>
                  <DetailTh>내용:</DetailTh>
                  <DetailTd>{selectedReport.content || '정보 없음'}</DetailTd>
                </DetailRow>
                <DetailRow>
                  <DetailTh>신고 유형:</DetailTh>
                  <DetailTd>{selectedReport.reportType || '정보 없음'}</DetailTd>
                </DetailRow>
                <DetailRow>
                  <DetailTh>신고 상태:</DetailTh>
                  <DetailTd>{selectedReport.reportStatus || '정보 없음'}</DetailTd>
                </DetailRow>
                <DetailRow>
                  <DetailTh>신고일자:</DetailTh>
                  <DetailTd>{new Date(
                      selectedReport.createAt).toLocaleString()}</DetailTd>
                </DetailRow>
                <DetailRow>
                  <DetailTh>처리일자:</DetailTh>
                  <DetailTd>
                    {selectedReport.reportStatus === 'COMPLETE'
                        ? new Date(selectedReport.updateAt).toLocaleString()
                        : '처리되지 않음'}
                  </DetailTd>
                </DetailRow>
                </tbody>
              </DetailTable>
              <ButtonContainer>
                <BackButton onClick={handleBackToList}>목록으로 돌아가기</BackButton>
                {selectedReport.reportStatus !== 'COMPLETE' && (
                    <CompleteButton onClick={handleCompleteStatus}>완료
                      처리</CompleteButton>
                )}
              </ButtonContainer>
            </DetailContainer>
        ) : (
            <Table>
              <thead>
              <tr>
                <Th style={{width: '5%'}}>번호</Th>
                <Th style={{width: '30%'}}>제목</Th>
                <Th style={{width: '15%'}}>신고자</Th>
                <Th style={{width: '15%'}}>신고받은자</Th>
                <Th style={{width: '15%'}}>신고일자</Th>
                <Th style={{width: '20%'}}>신고 상태</Th>
              </tr>
              </thead>
              <tbody>
              {reportedUsers.map((report, index) => (
                  <Tr key={report.reportId}
                      onClick={() => handleReportClick(report)}>
                    <Td>{reportedUsers.length - index}</Td>
                    <Td>{report.title || '정보 없음'}</Td>
                    <Td>{report.username}</Td>
                    <Td>{report.badPerson}</Td>
                    <Td>{new Date(report.createAt).toLocaleString()}</Td>
                    <Td>{report.reportStatus}</Td>
                  </Tr>
              ))}
              {!reportedUsers.length && (
                  <Tr>
                    <Td colSpan="6">신고된 유저가 없습니다.</Td>
                  </Tr>
              )}
              </tbody>
            </Table>
        )}
      </Container>
  );
};

export default ReportedUsers;

// 스타일링 정의
const Container = styled.div`
  padding: 20px;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  max-width: 1000px;
  margin: 0 auto;
`;

const Table = styled.table`
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
  table-layout: fixed;
`;

const Th = styled.th`
  background-color: #f1f3f5;
  padding: 8px;
  text-align: center;
  font-weight: bold;
  color: #495057;
  border-bottom: 2px solid #dee2e6;
`;

const Tr = styled.tr`
  cursor: pointer;

  &:nth-child(even) {
    background-color: #f8f9fa;
  }

  &:hover {
    background-color: #e9ecef;
  }
`;

const Td = styled.td`
  padding: 8px;
  text-align: center;
  color: #343a40;
  border: none;
`;

const ErrorText = styled.p`
  color: red;
  font-size: 14px;
  margin-top: 10px;
`;

const DetailContainer = styled.div`
  margin-top: 20px;
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
`;

const DetailTitle = styled.h3`
  margin-bottom: 20px;
  font-size: 1.5rem;
  color: #343a40;
  border-bottom: 2px solid #dee2e6;
  padding-bottom: 10px;
`;

const DetailTable = styled.table`
  width: 100%;
  margin-bottom: 20px;
`;

const DetailRow = styled.tr``;

const DetailTh = styled.th`
  text-align: left;
  padding: 10px;
  font-weight: bold;
  color: #495057;
  background-color: #f1f3f5;
  border: 1px solid #dee2e6;
`;

const DetailTd = styled.td`
  text-align: left;
  padding: 10px;
  border: 1px solid #dee2e6;
  background-color: #ffffff;
  color: #343a40;
`;

const ButtonContainer = styled.div`
  display: flex;
  justify-content: space-between;
`;

const BackButton = styled.button`
  padding: 10px 20px;
  background-color: #6c757d;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;

  &:hover {
    background-color: #5a6268;
  }
`;

const CompleteButton = styled.button`
  padding: 10px 20px;
  background-color: #28a745;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;

  &:hover {
    background-color: #218838;
  }
`;
