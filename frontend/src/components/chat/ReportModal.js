import React, {useEffect, useRef, useState} from 'react';

const ReportModal = ({isOpen, onClose, onSubmit}) => {
  const [reportType, setReportType] = useState('');
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const reportTitleRef = useRef(null);

  useEffect(() => {
    if (isOpen) {
      // 모달이 열리면 제목 입력 필드에 포커스를 맞춥니다.
      reportTitleRef.current.focus();
    }
  }, [isOpen]);

  const handleSubmit = (event) => {
    event.preventDefault();

    const reportData = {
      reportType,
      title,
      content,
    };

    onSubmit(reportData);
    onClose();
  };

  if (!isOpen) {
    return null; // 모달이 열리지 않았을 때는 아무것도 렌더링하지 않습니다.
  }

  return (
      <div className="modal">
        <div className="modal-content">
          <h2>Report</h2>
          <form onSubmit={handleSubmit}>
            <label>
              Type:
              <select value={reportType}
                      onChange={(e) => setReportType(e.target.value)}>
                <option value="spam">Spam</option>
                <option value="abuse">Abuse</option>
                <option value="other">Other</option>
              </select>
            </label>
            <label>
              Title:
              <input
                  type="text"
                  value={title}
                  onChange={(e) => setTitle(e.target.value)}
                  ref={reportTitleRef}
              />
            </label>
            <label>
              Content:
              <textarea value={content}
                        onChange={(e) => setContent(e.target.value)}/>
            </label>
            <button type="submit">Submit</button>
            <button type="button" onClick={onClose}>Cancel</button>
          </form>
        </div>
      </div>
  );
};

export default ReportModal;
