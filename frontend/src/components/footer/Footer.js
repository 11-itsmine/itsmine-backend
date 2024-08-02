import React from 'react';
import {FaGithub, FaInstagram} from 'react-icons/fa';
import {RiNotionFill} from "react-icons/ri";
import styled from 'styled-components';

const Footer = () => {
  return (
      <FooterContainer>
        <FooterIcons>
          <FooterLink
              href="https://www.instagram.com/leebeanbin/"
              target="_blank" rel="noopener noreferrer">
            <FaInstagram/>
          </FooterLink>
          <FooterLink href="https://github.com/11-itsmine/itsmine.git"
                      target="_blank" rel="noopener noreferrer">
            <FaGithub/>
          </FooterLink>
          <FooterLink
              href="https://www.notion.so/teamsparta/faf10b31eec04c97a18819caad6e1a54?pvs=4"
              target="_blank"
              rel="noopener noreferrer">
            <RiNotionFill/>
          </FooterLink>
        </FooterIcons>
        <Contents>이용안내</Contents>
        <Contents>고객지원</Contents>
        <Contents>고객센터 1522-8016</Contents>
        <ContentDetails>
          Itsmine · 팀장 김성찬, 부팀장 주장현, 팀원 이정빈, 이순모, 김동우 :<br/>
        </ContentDetails>
        <Reserved> © NB Camp. </Reserved>
      </FooterContainer>
  );
};

export default Footer;

const FooterContainer = styled.footer`
  padding-top: ${props => props.theme.paddings.xxxl};
  text-align: center;
`;

const FooterIcons = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: ${props => props.theme.margins.xxl};
  cursor: pointer;

  svg {
    width: 40px;
    height: 40px;
    margin: 0 10px;
  }
`;

const FooterLink = styled.a`
  color: inherit;
  text-decoration: none;

  &:hover {
    color: ${props => props.theme.colors.primary};
  }
`;

const Contents = styled.span`
  display: block;
  margin-top: ${props => props.theme.margins.xxxl};
  padding: ${props => props.theme.paddings.large};
  cursor: pointer;
`;

const ContentDetails = styled.div`
  font-size: ${props => props.theme.fontSizes.small};
  padding: ${props => props.theme.paddings.xl};
  color: gray;
`;

const Reserved = styled.div`
  font-weight: bold;
  padding: ${props => props.theme.paddings.xl};
  margin-bottom: ${props => props.theme.margins.xxxl};
`;
