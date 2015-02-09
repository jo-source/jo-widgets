<?xml version='1.0'?> 
<xsl:stylesheet  
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fo="http://www.w3.org/1999/XSL/Format"
    xmlns:xslthl="http://xslthl.sf.net"
    exclude-result-prefixes="xslthl"
	version='1.0'> 

<xsl:import href="file:///c:/docbook-xsl-1.78.1/html/chunk.xsl"/> 
<xsl:import href="file:///c:/docbook-xsl-1.78.1/html/highlight.xsl"/> 

<xsl:param name="l10n.gentext.language" select="de"/>

<xsl:param name="base.dir" select="'html_out\'"/>

<xsl:param name="use.extensions" select="1"/>
<xsl:param name="admon.graphics" select="1"/>

<xsl:param name="chunker.output.indent" select="'yes'"/>
<xsl:param name="chunk.section.depth" select="4"/>
<xsl:param name="chunk.first.sections" select="1"/>

<xsl:param name="section.autolabel" select="1"/>
<xsl:param name="section.label.includes.component.label" select="1"/>

<xsl:param name="highlight.source" select="1"/>
<xsl:param name="linenumbering" select="'numbered'"/>
<xsl:param name="linenumbering.everyNth" select="1"/>
<xsl:param name="linenumbering.separator" select="'  '"/>

<xsl:param name="html.stylesheet" select="'style.css'"/> 

<xsl:template match="xslthl:keyword" mode="xslthl">
    <strong class="hl-keyword">
	<em style="color:#950055">
      <xsl:apply-templates mode="xslthl"/>
	</em>
    </strong>
  </xsl:template>
  <xsl:template match="xslthl:string" mode="xslthl">
    <strong class="hl-string">
      <em style="color:#2A00FF">
        <xsl:apply-templates mode="xslthl"/>
      </em>
    </strong>
  </xsl:template>
  <xsl:template match="xslthl:comment" mode="xslthl">
    <em class="hl-comment" style="color:#3F7F5F">
      <xsl:apply-templates mode="xslthl"/>
    </em>
  </xsl:template>
  <xsl:template match="xslthl:directive" mode="xslthl">
    <span class="hl-directive" style="color: maroon">
      <xsl:apply-templates mode="xslthl"/>
    </span>
  </xsl:template>
  <xsl:template match="xslthl:tag" mode="xslthl">
    <strong class="hl-tag" style="color: #000096">
      <xsl:apply-templates mode="xslthl"/>
    </strong>
  </xsl:template>
  <xsl:template match="xslthl:attribute" mode="xslthl">
    <span class="hl-attribute" style="color: #F5844C">
      <xsl:apply-templates mode="xslthl"/>
    </span>
  </xsl:template>
  <xsl:template match="xslthl:value" mode="xslthl">
    <span class="hl-value" style="color: #993300">
      <xsl:apply-templates mode="xslthl"/>
    </span>
  </xsl:template>
  <xsl:template match='xslthl:html' mode="xslthl">
    <span class="hl-html" style="color: navy; font-weight: bold">
      <xsl:apply-templates mode="xslthl"/>
    </span>
  </xsl:template>
  <xsl:template match="xslthl:xslt" mode="xslthl">
    <strong style="color: #0066FF">
      <xsl:apply-templates mode="xslthl"/>
    </strong>
  </xsl:template>
  <!-- Not emitted since XSLTHL 2.0 -->
  <xsl:template match="xslthl:section" mode="xslthl">
    <strong>
      <xsl:apply-templates mode="xslthl"/>
    </strong>
  </xsl:template>
  <xsl:template match="xslthl:number" mode="xslthl">
    <span class="hl-number">
      <xsl:apply-templates mode="xslthl"/>
    </span>
  </xsl:template>
  <xsl:template match="xslthl:annotation" mode="xslthl">
    <em>
      <span class="hl-annotation" style="color:#646464">
        <xsl:apply-templates mode="xslthl"/>
      </span>
    </em>
  </xsl:template>
  <!-- Not sure which element will be in final XSLTHL 2.0 -->
  <xsl:template match="xslthl:doccomment|xslthl:doctype" mode="xslthl">
    <strong class="hl-tag" style="color: blue">
      <xsl:apply-templates mode="xslthl"/>
    </strong>
  </xsl:template>

</xsl:stylesheet>  