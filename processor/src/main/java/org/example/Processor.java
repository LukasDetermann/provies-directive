package org.example;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ModuleElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

@SupportedSourceVersion(SourceVersion.RELEASE_11)
@SupportedAnnotationTypes("*")
public class Processor extends AbstractProcessor
{
   @Override
   public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
   {
      if (!roundEnv.processingOver())
      {
         return false;
      }

      ModuleElement javaDesktop = processingEnv.getElementUtils().getModuleElement("java.desktop");
      javaDesktop.getDirectives()
                 .stream()
                 .filter(directive -> directive.getKind().equals(ModuleElement.DirectiveKind.PROVIDES))
                 .map(ModuleElement.ProvidesDirective.class::cast)
                 .filter(providesDirective -> providesDirective.getService()
                                                               .getQualifiedName()
                                                               .toString()
                                                               .equals("javax.sound.midi.spi.MidiDeviceProvider"))
                 .forEach(providesDirective -> processingEnv.getMessager()
                                                            .printMessage(Diagnostic.Kind.MANDATORY_WARNING, providesDirective.toString()));

      return false;
   }
}
