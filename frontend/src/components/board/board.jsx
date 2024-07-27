import React, { useState, useEffect, useRef, useCallback } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import BoardForm from './BoardForm'; // BoardForm 컴포넌트 import

function Board({ onLogout }) {
    const [boards, setBoards] = useState([]); // 보드 데이터 상태
    const [loading, setLoading] = useState(true); // 데이터 로딩 상태
    const [showPopup, setShowPopup] = useState(false); // 팝업 모달 상태
    const [editBoard, setEditBoard] = useState(null); // 수정할 보드 상태
    const [isEditMode, setIsEditMode] = useState(false); // 수정 모드 상태
    const [page, setPage] = useState(0); // 현재 페이지 상태
    const [hasMore, setHasMore] = useState(true); // 추가 데이터 여부 상태
    const [fetching, setFetching] = useState(false); // 데이터 가져오는 중 여부 상태
    const [userRole, setUserRole] = useState(null); // 사용자 권한 상태

    const observer = useRef(); // Intersection Observer ref

    const fetchBoardData = async (newPage = 0) => {
        if (fetching) return; // 이미 데이터를 가져오고 있는 중이면 중복 요청 방지

        const token = localStorage.getItem('Authorization');
        try {
            setFetching(true); // 데이터 가져오는 중으로 설정

            // API 호출하여 보드 데이터 가져오기
            const response = await axios.get('https://localhost:443/api/boards', {
                headers: {
                    Authorization: token // 토큰을 헤더에 포함해서 보냄
                },
                params: {
                    page: newPage
                }
            });
            console.log('보드 데이터 가져오기 성공:', response.data);

            if (response.data.data.content.length === 0) {
                setHasMore(false); // 가져올 데이터가 없으면 더 이상 가져올 필요 없음
            } else {
                setBoards(prevBoards => [...prevBoards, ...response.data.data.content]); // 기존 보드 데이터에 추가
                setPage(newPage + 1); // 페이지 증가
            }

            setLoading(false); // 로딩 상태 변경
            setFetching(false); // 데이터 가져오는 중 해제
        } catch (error) {
            console.error('보드 데이터 가져오기 실패:', error);
            setLoading(false); // 에러 발생 시에도 로딩 상태 변경
            setFetching(false); // 데이터 가져오는 중 해제
        }
    };

    const fetchUserRole = async () => {
        const token = localStorage.getItem('Authorization');
        try {
            const response = await axios.get('https://localhost:443/api/users', {
                headers: {
                    Authorization: token // 토큰을 헤더에 포함해서 보냄
                }
            });
            console.log('사용자 역할 가져오기 성공:', response.data.data.role);
            setUserRole(response.data.data.role); // 사용자 역할 설정
        } catch (error) {
            console.error('사용자 역할 가져오기 실패:', error);
        }
    };

    useEffect(() => {
        fetchBoardData(0); // 초기 페이지 로드
        fetchUserRole(); // 사용자 역할 로드
    }, []); // 컴포넌트 마운트 시 한 번만 실행

    const handleBoardClick = (id) => {
        // 보드 ID를 로컬 스토리지에 저장
        localStorage.setItem('board', id);
        console.log(`저장된 보드 ID: ${id}`);
    };

    const handleLogout = () => {
        localStorage.removeItem('board');
        onLogout();
    };

    const handleAddBoard = () => {
        setEditBoard(null); // 추가 모드일 때는 보드 데이터가 없으므로 null 설정
        setIsEditMode(false); // 추가 모드로 설정
        setShowPopup(true); // 팝업창 열기
    };

    const handleClosePopup = (isDataChanged) => {
        setShowPopup(false); // 팝업창을 닫을 때
        if (isDataChanged) {
            setBoards([]); // 보드 데이터를 초기화하고
            setPage(0); // 페이지를 초기화하여 데이터를 새로 가져오기
            fetchBoardData(0); // 초기 페이지로 데이터 가져오기
            window.location.reload(); // 페이지 새로고침
        }
    };

    const handleDeleteBoard = async (boardId) => {
        const confirmDelete = window.confirm("정말로 이 보드를 삭제하시겠습니까?");
        if (confirmDelete) {
            const token = localStorage.getItem('Authorization');
            try {
                await axios.delete(`https://localhost:443/api/boards/${boardId}`, {
                    headers: {
                        Authorization: token
                    }
                });
                // 삭제 성공 시, 업데이트된 보드 목록 다시 가져오기
                setBoards([]);
                setPage(0);
                fetchBoardData(0); // 초기 페이지로 데이터 가져오기
                window.location.reload(); // 페이지 새로고침
            } catch (error) {
                console.error('보드 삭제 실패:', error.response.data.message);
                // 에러 처리
                alert(error.response.data.message);
                const errorMessage = JSON.parse(error.request.responseText).message;
                alert(`${errorMessage}`);
            }
        }
    };

    const handleEditBoard = (board) => {
        setEditBoard(board); // 수정할 보드 설정
        setIsEditMode(true); // 수정 모드로 설정
        setShowPopup(true); // 팝업창 열기
    };

    const lastBoardElementRef = useCallback(node => {
        if (loading) return;
        if (observer.current) observer.current.disconnect();
        observer.current = new IntersectionObserver(entries => {
            if (entries[0].isIntersecting && hasMore) {
                fetchBoardData(page);
            }
        });
        if (node) observer.current.observe(node);
    }, [loading, hasMore, page]);

    return (
        <div>
            <header>
                <h1>보더 페이지</h1>
                <Link to="/">
                    <button onClick={handleLogout}>로그아웃</button>
                </Link>
                <Link to="/userProfile">
                    <button>유저 프로필</button>
                </Link>
                {userRole === 'MANAGER' && (
                    <button onClick={handleAddBoard}>보드 추가</button>
                )}
            </header>
            <main style={styles.boardContainer}>
                <div style={styles.boardGrid}>
                    {boards.length > 0 ? (
                        boards.map((board, index) => {
                            if (boards.length === index + 1) {
                                return (
                                    <div ref={lastBoardElementRef} key={board.id} style={styles.boardItem}>
                                        <Link to={`/boardDetail/${board.id}`} style={{ textDecoration: 'none', color: 'inherit' }}>
                                            <h2 onClick={() => handleBoardClick(board.id)}>{board.name}</h2>
                                        </Link>
                                        <p>{board.description}</p>
                                        <p>작성일: {board.createdAt}</p>
                                        <p>수정일: {board.modifiedAt}</p>
                                        {userRole === 'MANAGER' && (
                                            <>
                                                <button onClick={() => handleEditBoard(board)}>수정</button>
                                                <button onClick={() => handleDeleteBoard(board.id)}>삭제</button>
                                            </>
                                        )}
                                    </div>
                                );
                            } else {
                                return (
                                    <div key={board.id} style={styles.boardItem}>
                                        <Link to={`/boardDetail/${board.id}`} style={{ textDecoration: 'none', color: 'inherit' }}>
                                            <h2 onClick={() => handleBoardClick(board.id)}>{board.name}</h2>
                                        </Link>
                                        <p>{board.description}</p>
                                        <p>작성일: {board.createdAt}</p>
                                        <p>수정일: {board.modifiedAt}</p>
                                        {userRole === 'MANAGER' && (
                                            <>
                                                <button onClick={() => handleEditBoard(board)}>수정</button>
                                                <button onClick={() => handleDeleteBoard(board.id)}>삭제</button>
                                            </>
                                        )}
                                    </div>
                                );
                            }
                        })
                    ) : (
                        <p>사용 가능한 보드가 없습니다.</p>
                    )}
                    {loading && <p>로딩 중...</p>}
                </div>
            </main>
            {showPopup && (
                <BoardForm
                    onClose={handleClosePopup}
                    board={editBoard}
                    mode={isEditMode ? 'edit' : 'add'} // 수정 모드일 때 'edit' 전달
                />
            )}
        </div>
    );
}

const styles = {
    boardContainer: {
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
    },
    boardGrid: {
        display: 'grid',
        gridTemplateColumns: 'repeat(4, 1fr)',
        gap: '20px',
        maxWidth: '1200px',
        margin: '0 auto',
    },
    boardItem: {
        border: '1px solid #ccc',
        padding: '10px',
        borderRadius: '5px',
        cursor: 'pointer',
    },
};

export default Board;
