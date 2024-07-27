import React, { useState, useEffect } from 'react';
import axios from 'axios';

function BoardForm({ onClose, board = {}, mode = 'add' }) {
    const [formData, setFormData] = useState({
        name: '',
        description: ''
    });

    useEffect(() => {
        if (mode === 'edit' && board) {
            setFormData({
                name: board.name || '',
                description: board.description || ''
            });
        }
    }, [board, mode]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const token = localStorage.getItem('Authorization');
        const url = mode === 'add'
            ? 'https://localhost:443/api/boards'
            : `https://localhost:443/api/boards/${board.id}`;
        const method = mode === 'add' ? 'post' : 'put';

        try {
            await axios({
                method,
                url,
                data: formData,
                headers: {
                    Authorization: token
                }
            });
            console.log(`${mode === 'add' ? 'Board added' : 'Board updated'} successfully!`);
            onClose(true); // 팝업창 닫기 및 부모 컴포넌트에 변경 사항 알리기
        } catch (error) {
            console.error(`${mode === 'add' ? 'Failed to add board' : 'Failed to update board'}:`, error);
        }
    };

    return (
        <div style={styles.overlay}>
            <div style={styles.popup}>
                <h2>{mode === 'add' ? '보드 추가' : '보드 수정'}</h2>
                <form onSubmit={handleSubmit}>
                    <label>
                        Name:
                        <input type="text" name="name" value={formData.name} onChange={handleChange} required />
                    </label>
                    <br />
                    <label>
                        Description:
                        <input type="text" name="description" value={formData.description} onChange={handleChange} required />
                    </label>
                    <br />
                    <button type="submit">{mode === 'add' ? '추가' : '수정'}</button>
                    <button type="button" onClick={() => onClose(false)}>닫기</button> {/* 취소 버튼 */}
                </form>
            </div>
        </div>
    );
}

const styles = {
    overlay: {
        position: 'fixed',
        top: 0,
        left: 0,
        right: 0,
        bottom: 0,
        backgroundColor: 'rgba(0, 0, 0, 0.5)',
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center'
    },
    popup: {
        backgroundColor: '#fff',
        padding: '20px',
        borderRadius: '5px',
        boxShadow: '0 2px 10px rgba(0, 0, 0, 0.1)'
    }
};

export default BoardForm;
